package dbPackage;

import java.sql.*;

// DBConnection 클래스는 데이터베이스 연결을 관리하는 역할을 한다.
public class DBConnection {
    // 데이터베이스 연결에 필요한 상수 정의
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/librarydb"; // 데이터베이스 URL 설정
    private static final String USER = "root"; // 데이터베이스 사용자 이름 설정
    private static final String PASSWORD = "0000"; // 데이터베이스 비밀번호 설정

    // 데이터베이스 연결 인스턴스 (싱글톤 패턴으로 사용)
    private static Connection conn = null;

    // 생성자를 private으로 선언하여 외부에서 인스턴스화 불가능하도록 설정
    private DBConnection() {
    }

    // 데이터베이스에 연결을 반환하는 정적 메서드
    public static Connection getConnection() throws SQLException {
        // 연결이 없으면 새로운 연결을 생성
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName(DRIVER); // JDBC 드라이버 로드
                conn = DriverManager.getConnection(URL, USER, PASSWORD); // 연결 설정
                System.out.println("데이터베이스 연결 성공");
            } catch (ClassNotFoundException e) {
                System.out.println("JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("데이터베이스 연결 오류: " + e.getMessage());
                throw e; // 연결 오류 시 예외를 다시 던짐
            }
        }
        return conn; // 연결을 반환
    }

    // 데이터베이스 연결을 종료하는 메서드
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("데이터베이스 연결 종료");
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 종료 중 오류: " + e.getMessage());
        }
    }
}
