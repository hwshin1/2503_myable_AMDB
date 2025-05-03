package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 프로그램 시작 ==");

        Scanner sc = new Scanner(System.in);

        int lastArticleId = 0;
        List<Article> articles = new ArrayList<>();

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }

            if (cmd.equals("article write")) {
                System.out.println("== 글쓰기 ==");
                int id = 1;
                System.out.print("제목 : ");
                String title = sc.nextLine().trim();
                System.out.print("내용 : ");
                String body = sc.nextLine().trim();

                Article article = new Article(id, title, body);
                articles.add(article);

                lastArticleId++;
                System.out.println(article);

                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://localhost:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");

                    String sql = "INSERT INTO article";
                    sql += " SET regDate = now(),";
                    sql += " updateDate = now(),";
                    sql += " title = '" + title + "' ,";
                    sql += " `body` = '" + body + "' ;";

                    System.out.println(sql);
                    pstmt = conn.prepareStatement(sql);

                    int affectedRows = pstmt.executeUpdate();
                    System.out.println("DML시 실제로 영향을 미친 데이터 Row 수 : " + affectedRows);
                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else if (cmd.equals("article list")) {
                System.out.println("== 목록 ==");
                if (articles.size() == 0) {
                    System.out.println("게시글이 없습니다.");
                    continue;
                }
                System.out.println("   번호   /   제목   ");
                for (Article article : articles) {
                    System.out.printf("   %d   /   %s   \n", article.getId(), article.getTitle());
                }
            }
        }

        System.out.println("== 프로그램 종료 ==");
        sc.close();
    }
}
