-- V8__seed_borrow_transactions.sql
-- Description: Seeds various borrow transactions for testing history and overdue reports.

-- Helper variables for dates
-- Note: PostgreSQL `DATE 'YYYY-MM-DD'` is used for date literals
-- Today's date is assumed to be mid-December 2025 for realistic testing.

-------------------------------------
-- 1. Completed Transaction (User 5: Festus)
-------------------------------------
-- Borrowed: 30 days ago, Due: 16 days ago, Returned: 10 days ago (Returned on time)
INSERT INTO borrow_transactions (id, user_id, status, borrowed_date, due_date, returned_date) VALUES
    (1001, 5, 'COMPLETED', DATE '2025-11-17', DATE '2025-12-01', DATE '2025-12-07');

INSERT INTO borrowed_books (id, transaction_id, book_id, returned_date) VALUES
    (2001, 1001, 4, DATE '2025-12-07'); -- Sapiens (ID 4)

-- Update Book Count (Sapiens ID 4: total 3, available 2 -> should remain 2 if prior data is correct)


-------------------------------------
-- 2. Active, Overdue Transaction (User 7: Lucy)
-------------------------------------
-- Borrowed: 30 days ago, Due: 16 days ago, NOT returned (OVERDUE)
INSERT INTO borrow_transactions (id, user_id, status, borrowed_date, due_date, returned_date) VALUES
    (1002, 7, 'ACTIVE', DATE '2025-11-17', DATE '2025-12-01', NULL);

INSERT INTO borrowed_books (id, transaction_id, book_id, returned_date) VALUES
    (2002, 1002, 2, NULL); -- 1984 (ID 2)
-- This transaction tests: GET /librarian/overdue

-- Update Book Count (1984 ID 2: total 4, available 2 -> decrement available to 1)
UPDATE books SET available_copies = available_copies - 1 WHERE id = 2;


-------------------------------------
-- 3. Active, On-Time Transaction (User 8: Kenny)
-------------------------------------
-- Borrowed: 5 days ago, Due: 9 days from now, NOT returned (ACTIVE)
INSERT INTO borrow_transactions (id, user_id, status, borrowed_date, due_date, returned_date) VALUES
    (1003, 8, 'ACTIVE', DATE '2025-12-12', DATE '2025-12-30', NULL);

INSERT INTO borrowed_books (id, transaction_id, book_id, returned_date) VALUES
    (2003, 1003, 10, NULL); -- The Hobbit (ID 10)
-- This transaction tests: GET /users/{id}/borrow-history

-- Update Book Count (The Hobbit ID 10: total 7, available 5 -> decrement available to 4)
UPDATE books SET available_copies = available_copies - 1 WHERE id = 10;


-------------------------------------
-- 4. Transaction with Partially Returned Books (User 6: Jane/ADMIN)
-------------------------------------
-- Borrowed: 20 days ago, Due: 6 days ago (OVERDUE if unreturned)
INSERT INTO borrow_transactions (id, user_id, status, borrowed_date, due_date, returned_date) VALUES
    (1004, 6, 'ACTIVE', DATE '2025-11-27', DATE '2025-12-11', NULL);

INSERT INTO borrowed_books (id, transaction_id, book_id, returned_date) VALUES
                                                                            (2004, 1004, 11, DATE '2025-12-14'), -- Harry Potter (ID 11) - Returned Late (Fine Case)
                                                                            (2005, 1004, 12, NULL); -- The Shining (ID 12) - Still Out (OVERDUE)

-- This transaction tests: PUT /transactions/item/{borrowedBookId}/return (for item 2005)
-- This transaction also tests: GET /librarian/overdue (for item 2005)

-- Update Book Counts:
-- ID 11 (Harry Potter): total 8, available 3 -> returned 1, so available_copies should be 4
UPDATE books SET available_copies = available_copies + 1 WHERE id = 11;
-- ID 12 (The Shining): total 4, available 2 -> still out, so available_copies should be 1
UPDATE books SET available_copies = available_copies - 1 WHERE id = 12;

-- Update sequences for borrow_transactions and borrowed_books
SELECT setval('borrow_transactions_id_seq', (SELECT MAX(id) FROM borrow_transactions), true);
SELECT setval('borrowed_books_id_seq', (SELECT MAX(id) FROM borrowed_books), true);