-- ========================================================
-- DATABASE SCRIPT CHO ĐỀ THI QUÁ TRÌNH - ĐỀ SỐ 02
-- MÔN: LẬP TRÌNH WEB
-- SINH VIÊN: MSSV 24133034
-- ========================================================

-- Kiểm tra nếu database chưa có thì mới tạo mới
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'BookStore_24133034')
BEGIN
    CREATE DATABASE BookStore_24133034;
END
GO

USE BookStore_24133034;
GO

-- Xóa các bảng cũ nếu đã tồn tại để tránh xung đột
IF OBJECT_ID('dbo.order_details', 'U') IS NOT NULL DROP TABLE dbo.order_details;
IF OBJECT_ID('dbo.orders', 'U') IS NOT NULL DROP TABLE dbo.orders;
IF OBJECT_ID('dbo.rating', 'U') IS NOT NULL DROP TABLE dbo.rating;
IF OBJECT_ID('dbo.book_author', 'U') IS NOT NULL DROP TABLE dbo.book_author;
IF OBJECT_ID('dbo.author', 'U') IS NOT NULL DROP TABLE dbo.author;
IF OBJECT_ID('dbo.books', 'U') IS NOT NULL DROP TABLE dbo.books;
IF OBJECT_ID('dbo.users', 'U') IS NOT NULL DROP TABLE dbo.users;
GO

-- 1. Bảng users
CREATE TABLE dbo.users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    fullname NVARCHAR(50) NULL,
    phone INT NULL,
    passwd VARCHAR(32) NOT NULL,
    signup_date DATETIME DEFAULT GETDATE(),
    last_login DATETIME NULL,
    is_admin BIT DEFAULT 0,
    is_active BIT DEFAULT 1,
    otp_code VARCHAR(10) NULL
);
GO

-- 2. Bảng books
CREATE TABLE dbo.books (
    bookid INT IDENTITY(1,1) PRIMARY KEY,
    isbn INT NULL,
    title NVARCHAR(255) NULL,
    publisher NVARCHAR(255) NULL,
    price DECIMAL(12, 2) NULL,
    description NVARCHAR(MAX) NULL,
    publish_date DATE NULL,
    cover_image NVARCHAR(500) NULL,
    quantity INT NULL
);
GO

-- 3. Bảng author
CREATE TABLE dbo.author (
    author_id INT IDENTITY(1,1) PRIMARY KEY,
    author_name NVARCHAR(255) NULL,
    date_of_birth DATE NULL
);
GO

-- 4. Bảng book_author (Quan hệ N - N giữa books và author)
CREATE TABLE dbo.book_author (
    bookid INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_BookAuthor_Books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid) ON DELETE CASCADE,
    CONSTRAINT FK_BookAuthor_Author FOREIGN KEY (author_id) REFERENCES dbo.author(author_id) ON DELETE CASCADE
);
GO

-- 5. Bảng rating
CREATE TABLE dbo.rating (
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating TINYINT NULL,
    review_text TEXT NULL,
    PRIMARY KEY (userid, bookid),
    CONSTRAINT FK_Rating_Users FOREIGN KEY (userid) REFERENCES dbo.users(id) ON DELETE CASCADE,
    CONSTRAINT FK_Rating_Books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid) ON DELETE CASCADE
);
GO

-- ========================================================
-- DỮ LIỆU MẪU (TEST DATA)
-- ========================================================

-- Thêm users (Admin & User)
INSERT INTO dbo.users (email, fullname, phone, passwd, is_admin, is_active) VALUES
('admin@bookstore.vn', N'Quản Trị Viên', 901234567, '123456', 1, 1),
('user1@gmail.com', N'Nguyễn Văn A', 912345678, '123456', 0, 1),
('user2@gmail.com', N'Trần Thị B', 923456789, '123456', 0, 1),
('user3@gmail.com', N'Lê Hoàng C', 934567890, '123456', 0, 1);

-- Thêm Tác giả (Author)
INSERT INTO dbo.author (author_name, date_of_birth) VALUES
('J.K. Rowling', '1965-07-31'),
('Arthur Conan Doyle', '1859-05-22'),
('Dan Brown', '1964-06-22'),
('Haruki Murakami', '1949-01-12');

-- Thêm Sách (Books) - Đơn giá VNĐ
INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity) VALUES
-- Sách của J.K. Rowling (ID: 1, 2, 3, 4)
(1001, 'Harry Potter and the Sorcerer''s Stone', 'Bloomsbury', 120000.00, 'The story of a young wizard, Harry Potter.', '1997-06-26', 'hp1.jpg', 50),
(1002, 'Harry Potter and the Chamber of Secrets', 'Bloomsbury', 130000.00, 'Harry discovers the mystery of the Chamber of Secrets.', '1998-07-02', 'hp2.jpg', 40),
(1003, 'Harry Potter and the Prisoner of Azkaban', 'Bloomsbury', 140000.00, 'Sirius Black escapes from Azkaban prison.', '1999-07-08', 'hp3.jpg', 35),
(1004, 'Harry Potter and the Goblet of Fire', 'Bloomsbury', 150000.00, 'The Triwizard Tournament arrives at Hogwarts.', '2000-07-08', 'hp4.jpg', 25),

-- Sách của Arthur Conan Doyle (ID: 5, 6, 7, 8)
(2001, 'A Study in Scarlet', 'Ward Lock & Co', 80000.00, 'The first appearance of Sherlock Holmes and Dr. Watson.', '1887-11-01', 'sh1.jpg', 60),
(2002, 'The Sign of the Four', 'Spencer Blackett', 85000.00, 'Sherlock Holmes investigates a complex treasure plot.', '1890-02-01', 'sh2.jpg', 45),
(2003, 'The Hound of the Baskervilles', 'George Newnes', 95000.00, 'A legendary ghostly hound terrorizes Dartmoor.', '1902-03-25', 'sh3.jpg', 50),
(2004, 'The Valley of Fear', 'Smith Elder & Co', 90000.00, 'A secret society mystery solved by Holmes.', '1915-02-27', 'sh4.jpg', 30),

-- Sách của Dan Brown (ID: 9, 10, 11, 12)
(3001, 'Angels & Demons', 'Pocket Books', 110000.00, 'Robert Langdon investigates the Illuminati.', '2000-05-01', 'db1.jpg', 40),
(3002, 'The Da Vinci Code', 'Doubleday', 115000.00, 'A murder in the Louvre reveals an ancient secret.', '2003-03-18', 'db2.jpg', 70),
(3003, 'The Lost Symbol', 'Doubleday', 125000.00, 'Masonic secrets in Washington D.C.', '2009-09-15', 'db3.jpg', 35),
(3004, 'Inferno', 'Doubleday', 135000.00, 'A deadly plague plot based on Dante''s Inferno.', '2013-05-14', 'db4.jpg', 50);

-- Liên kết sách với tác giả (book_author)
INSERT INTO dbo.book_author (bookid, author_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1),
(5, 2), (6, 2), (7, 2), (8, 2),
(9, 3), (10, 3), (11, 3), (12, 3);

-- Thêm đánh giá mẫu (rating)
INSERT INTO dbo.rating (userid, bookid, rating, review_text) VALUES
(2, 1, 5, 'Great fantasy book, loved every chapter!'),
(3, 1, 4, 'Very intriguing world building.'),
(4, 1, 5, 'A masterpiece for all ages.'),
(3, 9, 4, 'Fast paced and suspenseful thrill ride.');
GO

-- 6. Bảng orders (Đơn hàng)
CREATE TABLE dbo.orders (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    receiver_name NVARCHAR(100) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address NVARCHAR(500) NOT NULL,
    note NVARCHAR(500) NULL,
    payment_method NVARCHAR(50) DEFAULT N'COD',
    total_money DECIMAL(12, 2) NOT NULL,
    status NVARCHAR(50) NOT NULL DEFAULT N'Đơn hàng mới',
    created_date DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Orders_Users FOREIGN KEY (user_id) REFERENCES dbo.users(id) ON DELETE CASCADE
);
GO

-- 7. Bảng order_details (Chi tiết đơn hàng)
CREATE TABLE dbo.order_details (
    order_detail_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    bookid INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT FK_OrderDetails_Orders FOREIGN KEY (order_id) REFERENCES dbo.orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_OrderDetails_Books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid) ON DELETE CASCADE
);
GO

-- Dữ liệu mẫu đơn hàng theo đủ 8 trạng thái yêu cầu
-- 1. Đơn hàng mới
-- 2. Đã xác nhận
-- 3. Chuẩn bị hàng
-- 4. Vận chuyển
-- 5. Giao hàng
-- 6. Đã giao
-- 7. Đơn hàng hủy
-- 8. Đơn hàng hoàn
INSERT INTO dbo.orders (user_id, receiver_name, receiver_phone, receiver_address, note, payment_method, total_money, status, created_date)
VALUES 
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'Giao giờ hành chính', N'COD', 250000, N'Đơn hàng mới', DATEADD(MINUTE, -10, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'Gọi trước khi giao', N'COD', 140000, N'Đã xác nhận', DATEADD(HOUR, -2, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'', N'COD', 270000, N'Chuẩn bị hàng', DATEADD(HOUR, -5, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'', N'COD', 175000, N'Vận chuyển', DATEADD(DAY, -1, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'Giao buổi chiều', N'COD', 80000, N'Giao hàng', DATEADD(DAY, -2, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'', N'COD', 235000, N'Đã giao', DATEADD(DAY, -3, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'Hủy do đổi ý', N'COD', 120000, N'Đơn hàng hủy', DATEADD(DAY, -4, GETDATE())),
(2, N'Nguyễn Văn A', '0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'Sách bị cấn góc xin hoàn', N'COD', 110000, N'Đơn hàng hoàn', DATEADD(DAY, -5, GETDATE()));

INSERT INTO dbo.order_details (order_id, bookid, price, quantity)
VALUES
(1, 1, 120000, 1),
(1, 2, 130000, 1),
(2, 3, 140000, 1),
(3, 4, 150000, 1),
(3, 1, 120000, 1),
(4, 5, 80000, 1),
(4, 7, 95000, 1),
(5, 5, 80000, 1),
(6, 9, 110000, 1),
(6, 11, 125000, 1),
(7, 1, 120000, 1),
(8, 9, 110000, 1);
GO
