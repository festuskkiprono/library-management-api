-- V7__seed_admin_rules_and_carts.sql
-- Description: Seeds a LIBRARIAN user, sets borrow rules, and creates active carts.

-------------------------------------
-- 1. Seed LIBRARIAN User
-------------------------------------
-- Note: We use a placeholder password hash here. In a real environment, you'd use a securely hashed password.
-- The existing users: 2, 5, 8, 7, 9, 6. We'll use ID 10 for the new user.
INSERT INTO library_users (id, name, email, password, role) VALUES
    (10, 'Librarian Emma', 'emma.lib@mail.com', '$2a$10$CgLLcYwUMr3xDMeJlmWg8u7X4iowq.WCdlEFrM1tzhjwCwqXxTfVG', 'LIBRARIAN');

-- Update sequences if using `BIGSERIAL` to prevent conflicts with manually inserted IDs
SELECT setval('library_users_id_seq', (SELECT MAX(id) FROM library_users), true);


-------------------------------------
-- 2. Seed Borrow Rules (for testing policies)
-------------------------------------
-- ID 1: Default rule (e.g., max 5 books for 14 days)
INSERT INTO borrow_rules (id, max_books, max_borrow_days) VALUES
    (1, 5, 14);


-------------------------------------
-- 3. Seed Active Carts (for testing CartController)
-------------------------------------
-- Cart for User 5 (Festus)
-- Books: To Kill a Mockingbird (ID 1, Copies 1), The Great Gatsby (ID 3, Copies 1)
-- Book IDs 1 and 3 are available.
INSERT INTO carts (id) VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'); -- Use a known UUID for easy testing

INSERT INTO cart_items (cart_id, book_id, copies) VALUES
                                                      ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 1, 1),
                                                      ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 3, 1);

-- Cart for User 8 (Kenny)
-- Book: The Da Vinci Code (ID 5, Copies 1)
-- Book ID 5 has only 1 available copy.
INSERT INTO carts (id) VALUES ('b1fcc10a-b1b0-4221-9d22-2cd45d4a1b22');

INSERT INTO cart_items (cart_id, book_id, copies) VALUES
    ('b1fcc10a-b1b0-4221-9d22-2cd45d4a1b22', 5, 1);