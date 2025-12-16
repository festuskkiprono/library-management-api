CREATE TABLE genres (
                        id SMALLSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(20) UNIQUE,
                       description TEXT,

                       genre_id SMALLINT,
                       total_copies INTEGER NOT NULL CHECK (total_copies >= 0),
                       available_copies INTEGER NOT NULL CHECK (available_copies >= 0),

                       CONSTRAINT fk_books_genre
                           FOREIGN KEY (genre_id)
                               REFERENCES genres(id)
);
CREATE TABLE library_users (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(150) NOT NULL,
                               email VARCHAR(255) NOT NULL UNIQUE,
                               password VARCHAR(255) NOT NULL,
                               role VARCHAR(20) NOT NULL
);
CREATE TABLE user_profiles (
                               id BIGINT PRIMARY KEY,
                               phone_number VARCHAR(20),
                               date_of_birth DATE,

                               CONSTRAINT fk_profile_user
                                   FOREIGN KEY (id)
                                       REFERENCES library_users(id)
                                       ON DELETE CASCADE
);


CREATE TABLE borrow_transactions (
                                     id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     status VARCHAR(30) NOT NULL,

                                     borrowed_date DATE NOT NULL,
                                     due_date DATE NOT NULL,
                                     returned_date DATE,

                                     CONSTRAINT fk_transaction_user
                                         FOREIGN KEY (user_id)
                                             REFERENCES library_users(id)
);
CREATE TABLE borrowed_books (
                                id BIGSERIAL PRIMARY KEY,
                                transaction_id BIGINT NOT NULL,
                                book_id BIGINT NOT NULL,
                                returned_date DATE,

                                CONSTRAINT fk_borrowed_transaction
                                    FOREIGN KEY (transaction_id)
                                        REFERENCES borrow_transactions(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_borrowed_book
                                    FOREIGN KEY (book_id)
                                        REFERENCES books(id)
);
CREATE TABLE borrow_rules (
                              id BIGINT PRIMARY KEY,
                              max_books INTEGER NOT NULL CHECK (max_books > 0),
                              max_borrow_days INTEGER NOT NULL CHECK (max_borrow_days > 0)
);
