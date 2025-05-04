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

                lastArticleId++;
                System.out.println(id + "번 글이 작성되었습니다.");

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

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;

                List<Article> articles = new ArrayList<>();

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://localhost:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");

                    String sql = "SELECT * ";
                    sql += "FROM article ";
                    sql += "ORDER BY id DESC";

                    System.out.println(sql);
                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String regDate = rs.getString("regDate");
                        String updateDate = rs.getString("updateDate");
                        String title = rs.getString("title");
                        String body = rs.getString("body");

                        Article article = new Article(id, regDate, updateDate, title, body);
                        articles.add(article);
                    }
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
