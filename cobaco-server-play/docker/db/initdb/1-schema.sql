CREATE TYPE file_type AS ENUM ('FILE', 'DIRECTORY');

CREATE TABLE IF NOT EXISTS file (
    id CHAR(36) PRIMARY KEY,
    name TEXT,
    path TEXT,
    type FILE_TYPE,
    content BYTEA,
    size BIGINT
);

CREATE TABLE IF NOT EXISTS user_account (
    id UUID PRIMARY KEY,
    email VARCHAR(254) UNIQUE,
    name VARCHAR(64),
    password VARCHAR(256),
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS file_ownership (
    file_id CHAR(36),
    owned_at UUID,
    type VARCHAR(20),
    PRIMARY KEY (file_id, owned_at)
);
