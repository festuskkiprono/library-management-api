-- V2__create_borrow_cart_tables.sql

CREATE TABLE borrow_carts
(
    id          UUID        DEFAULT gen_random_uuid() NOT NULL
        PRIMARY KEY,
    user_id     BIGINT      NOT NULL
        CONSTRAINT borrow_carts_users_fk
            REFERENCES users (id)
            ON DELETE CASCADE,

    status      VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE borrow_cart_items
(
    id          BIGSERIAL   PRIMARY KEY,
    cart_id     UUID        NOT NULL
        CONSTRAINT borrow_cart_items_cart_fk
            REFERENCES borrow_carts (id)
            ON DELETE CASCADE,

    book_id     BIGINT      NOT NULL
        CONSTRAINT borrow_cart_items_books_fk
            REFERENCES books (id)
            ON DELETE CASCADE,

    copies      INTEGER     NOT NULL
        CONSTRAINT borrow_cart_items_copies_check
            CHECK (copies > 0),

    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT borrow_cart_items_unique_cart_book
        UNIQUE (cart_id, book_id)
);

CREATE INDEX idx_borrow_cart_items_cart_id
    ON borrow_cart_items(cart_id);

CREATE INDEX idx_borrow_cart_items_book_id
    ON borrow_cart_items(book_id);

COMMENT ON TABLE borrow_carts IS 'Temporary carts holding books users plan to borrow';
COMMENT ON TABLE borrow_cart_items IS 'Books selected for borrowing';
COMMENT ON COLUMN borrow_cart_items.copies IS 'Number of copies requested';

