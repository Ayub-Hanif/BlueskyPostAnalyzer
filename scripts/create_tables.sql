-- create_tables.sql
-- This script creates a single "posts" table with a self-referencing parent_id.

CREATE TABLE IF NOT EXISTS posts (
    id SERIAL PRIMARY KEY,
    post_id VARCHAR(255) UNIQUE NOT NULL,
    content TEXT,
    word_count INT,
    created_at TIMESTAMP DEFAULT NOW(),
    parent_id INT REFERENCES posts(id) ON DELETE CASCADE,
    reply_count INT DEFAULT 0
);
