CREATE TABLE voters
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(50) NOT NULL,
    user_nid   VARCHAR(20) UNIQUE NOT NULL,
    address    TEXT,
    phone      VARCHAR(20) UNIQUE NOT NULL,
    dob        DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_nid ON voters (user_nid);
CREATE INDEX idx_users_phone ON voters (phone);
