-- =====================================================
-- Library Management System - MySQL Database Script
-- Run this in MySQL Workbench or MySQL CLI first!
-- =====================================================

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- 2. Create Books table
CREATE TABLE IF NOT EXISTS books (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    author      VARCHAR(150) NOT NULL,
    isbn        VARCHAR(20)  UNIQUE NOT NULL,
    category    VARCHAR(100),
    quantity    INT          DEFAULT 1,
    available   INT          DEFAULT 1,
    added_date  DATE         DEFAULT (CURDATE())
);

-- 3. Create Members table
CREATE TABLE IF NOT EXISTS members (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    email        VARCHAR(150) UNIQUE NOT NULL,
    phone        VARCHAR(20),
    address      TEXT,
    join_date    DATE         DEFAULT (CURDATE()),
    is_active    BOOLEAN      DEFAULT TRUE
);

-- 4. Create Borrow Records table
CREATE TABLE IF NOT EXISTS borrow_records (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    book_id       INT NOT NULL,
    member_id     INT NOT NULL,
    borrow_date   DATE DEFAULT (CURDATE()),
    due_date      DATE,
    return_date   DATE,
    status        ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    FOREIGN KEY (book_id)   REFERENCES books(id)   ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 5. Insert sample books
INSERT INTO books (title, author, isbn, category, quantity, available) VALUES
('Clean Code',                    'Robert C. Martin',   '9780132350884', 'Programming',  3, 3),
('The Pragmatic Programmer',      'Andrew Hunt',        '9780201616224', 'Programming',  2, 2),
('Introduction to Algorithms',    'Thomas H. Cormen',   '9780262033848', 'Computer Science', 2, 2),
('Design Patterns',               'Gang of Four',       '9780201633610', 'Software Engineering', 1, 1),
('Java: The Complete Reference',  'Herbert Schildt',    '9781260440232', 'Java',         4, 4),
('Database System Concepts',      'Abraham Silberschatz','9780078022159','Database',     2, 2);

-- 6. Insert sample members
INSERT INTO members (name, email, phone, address) VALUES
('Abebe Girma',    'abebe@gmail.com',   '0911234567', 'Addis Ababa, Bole'),
('Tigist Alemu',   'tigist@gmail.com',  '0922345678', 'Addis Ababa, Kazanchis'),
('Dawit Bekele',   'dawit@gmail.com',   '0933456789', 'Addis Ababa, Piassa');

-- 7. Verify
SELECT 'Books:'   AS TableName, COUNT(*) AS Total FROM books
UNION ALL
SELECT 'Members:', COUNT(*) FROM members;
