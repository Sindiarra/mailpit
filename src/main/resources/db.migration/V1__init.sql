-- V1__init.sql

CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_email UNIQUE (email) --  contrainte explicite
);

CREATE TABLE activation_codes (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    user_id UUID NOT NULL,
    CONSTRAINT fk_activation_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
