-- V9__consolidate_user_tables.sql
-- Description: Consolidate user data storage by re-pointing FKs to the 'users' table

------------------------------------------------------
-- 1. DROP EXISTING FOREIGN KEYS (SAFE)
------------------------------------------------------
ALTER TABLE borrow_transactions
DROP CONSTRAINT IF EXISTS borrow_transactions_user_id_fkey;

ALTER TABLE user_profiles
DROP CONSTRAINT IF EXISTS user_profiles_id_fkey;

------------------------------------------------------
-- 2. RE-CREATE CONSTRAINTS POINTING TO users
------------------------------------------------------
ALTER TABLE borrow_transactions
    ADD CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id)
            REFERENCES users (id);

ALTER TABLE user_profiles
    ADD CONSTRAINT fk_profile_user
        FOREIGN KEY (id)
            REFERENCES users (id)
            ON DELETE CASCADE;

------------------------------------------------------
-- 3. DROP OLD TABLE
------------------------------------------------------
DROP TABLE IF EXISTS library_users CASCADE;
