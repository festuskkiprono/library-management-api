
INSERT INTO genres (name) VALUES
('Fiction'),
('Non-Fiction'),
('Science Fiction'),
('Mystery'),
('Romance'),
('Thriller'),
('Biography'),
('History'),
('Self-Help'),
('Fantasy'),
('Horror'),
('Poetry'),
('Science'),
('Technology'),
('Business');


INSERT INTO books (title, author, isbn, description, genre_id, total_copies, available_copies) VALUES
('To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 'A gripping tale of racial injustice and childhood innocence in the American South.', 1, 5, 3),
('1984', 'George Orwell', '978-0451524935', 'A dystopian social science fiction novel and cautionary tale about totalitarianism.', 3, 4, 2),
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'A portrait of the Jazz Age in all of its decadence and excess.', 1, 6, 4),
('Sapiens', 'Yuval Noah Harari', '978-0062316097', 'A brief history of humankind from the Stone Age to the modern age.', 2, 3, 2),
('The Da Vinci Code', 'Dan Brown', '978-0307474278', 'A mystery thriller novel following symbologist Robert Langdon.', 4, 4, 1),
('Pride and Prejudice', 'Jane Austen', '978-0141439518', 'A romantic novel of manners set in Georgian England.', 5, 5, 5),
('Gone Girl', 'Gillian Flynn', '978-0307588371', 'A psychological thriller about a marriage gone terribly wrong.', 6, 3, 0),
('Steve Jobs', 'Walter Isaacson', '978-1451648539', 'The exclusive biography of the Apple co-founder.', 7, 2, 1),
('A Brief History of Time', 'Stephen Hawking', '978-0553380163', 'A landmark volume in science writing exploring the universe.', 13, 3, 2),
('The Hobbit', 'J.R.R. Tolkien', '978-0345339683', 'A fantasy novel about the quest of home-loving Bilbo Baggins.', 10, 7, 5),
('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', '978-0439708180', 'The first novel in the Harry Potter series.', 10, 8, 3),
('The Shining', 'Stephen King', '978-0307743657', 'A horror novel about a family isolated in a haunted hotel.', 11, 4, 2),
('Atomic Habits', 'James Clear', '978-0735211292', 'An easy and proven way to build good habits and break bad ones.', 9, 5, 3),
('The Lean Startup', 'Eric Ries', '978-0307887894', 'How today''s entrepreneurs use continuous innovation to create radically successful businesses.', 15, 3, 2),
('Dune', 'Frank Herbert', '978-0441172719', 'A science fiction novel set in the distant future amidst a feudal interstellar society.', 3, 4, 1);