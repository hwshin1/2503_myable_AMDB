package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public void run() {
        System.out.println("== 프로그램 시작 ==");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://localhost:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            try {
                conn = DriverManager.getConnection(url, "root", "");
                int actionResult = doAction(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("== 프로그램 종료 ==");
                    sc.close();
                    break;
                }
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
        }
    }

    public int doAction(Connection conn, Scanner sc, String cmd) {
        if (cmd.equals("exit")) {
            return -1;
        }

        if (cmd.equals("article write")) {
            System.out.println("== 글쓰기 ==");
            System.out.print("제목 : ");
            String title = sc.nextLine().trim();
            System.out.print("내용 : ");
            String body = sc.nextLine().trim();

            PreparedStatement pstmt = null;

            try {
                String sql = "INSERT INTO article";
                sql += " SET regDate = now(),";
                sql += " updateDate = now(),";
                sql += " title = '" + title + "' ,";
                sql += " `body` = '" + body + "' ;";

                System.out.println(sql);
                pstmt = conn.prepareStatement(sql);

                int affectedRows = pstmt.executeUpdate();
                System.out.println(affectedRows + "열에 적용되었습니다.");
            } catch (SQLException e) {
                System.out.println("에러 write : " + e);
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

            PreparedStatement pstmt = null;
            ResultSet rs = null;

            List<Article> articles = new ArrayList<>();

            try {
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
            } catch (SQLException e) {
                System.out.println("에러 list : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (articles.size() == 0) {
                System.out.println("게시글이 없습니다.");
                return 0;
            }
            System.out.println("   번호   /   제목   ");
            for (Article article : articles) {
                System.out.printf("   %d   /   %s   \n", article.getId(), article.getTitle());
            }
        } else if (cmd.startsWith("article modify")) {
            int id = 0;

            // 있는지 없는지 체크
            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("정수를 입력해 주세요." + e);
                return 0;
            }

            System.out.println("== 글 수정 ==");
            System.out.print("새 제목 : ");
            String title = sc.nextLine().trim();
            System.out.print("새 내용 : ");
            String body = sc.nextLine().trim();

            PreparedStatement pstmt = null;

            try {
                String sql = "UPDATE article ";
                sql += "SET updateDate = now(),";
                if (!title.isEmpty()) {
                    sql += " title = '" + title + "',";
                }
                if (!body.isEmpty()) {
                    sql += " `body` = '" + body + "'";
                }
                sql += " WHERE id = " + id + ";";

                System.out.println(sql);
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("에러 modify : " + e);
            } finally {
                try {
                    if (pstmt != null && !pstmt.isClosed()) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(id + "번 글이 수정되었습니다.");
        }
        return 0;
    }
}
