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


CREATE TABLE `member` (
                          id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          regDate DATETIME NOT NULL,
                          updateDate DATETIME NOT NULL,
                          loginId CHAR(30) NOT NULL,
                          loginPw CHAR(200) NOT NULL,
                          `name` CHAR(100) NOT NULL
);

SELECT * FROM article;

INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = '제목1',
    `body` = '내용1';

INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = '제목2',
    `body` = '내용2';

INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = '제목3',
    `body` = '내용3';

SELECT * FROM article
ORDER BY id DESC;

SELECT * FROM `member`;

INSERT INTO `member`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'test1',
    loginPw = 'test1',
    `name` = '홍길동';
