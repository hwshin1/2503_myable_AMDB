DROP DATABASE IF EXISTS JDBC;
CREATE DATABASE JDBC;
USE JDBC;

DROP TABLE IF EXISTS article;
CREATE TABLE article(
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        regDate DATETIME NOT NULL,
                        updateDate DATETIME NOT NULL,
                        `title` VARCHAR(100) NOT NULL,
                        `body` TEXT
);

SELECT * FROM article;

INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = '제목1',
    `body` = '내용1';
