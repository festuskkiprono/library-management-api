-- V5__create_cart_table.sql

CREATE TABLE IF NOT EXISTS carts
(
    id          UUID        DEFAULT gen_random_uuid() NOT NULL
    PRIMARY KEY,

    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS cart_items
(
    id          BIGSERIAL PRIMARY KEY,
    cart_id     UUID    NOT NULL
    REFERENCES carts (id)
    ON DELETE CASCADE,

    book_id     BIGINT  NOT NULL
    REFERENCES books (id)
    ON DELETE CASCADE,

    copies      INTEGER NOT NULL CHECK (copies > 0),

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (cart_id, book_id)
    );

CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id
    ON cart_items(cart_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_book_id
    ON cart_items(book_id);

COMMENT ON TABLE carts IS 'Book cart for users';

COMMENT ON TABLE cart_items IS 'Books in borrowing cart';

COMMENT ON COLUMN cart_items.copies IS 'Must be greater than 0';