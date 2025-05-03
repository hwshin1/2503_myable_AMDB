DROP DATABASE IF EXISTS JDBC;
CREATE DATABASE JDBC;
USE JDBC;

DROP TABLE IF EXISTS article;
CREATE TABLE article(
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        `title` VARCHAR(100) NOT NULL,
                        `content` TEXT
);
