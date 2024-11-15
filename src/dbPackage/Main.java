package dbPackage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException {

        Menu JoinTitle = new Menu();
        Menu MainMenu = new Menu();

        // 접속 타이틀 화면
        JoinTitle.JoinTitle();
        
        // 3초 대기
        Thread.sleep(1000);

        // 메인 메뉴 호출
        MainMenu.MainMenu();
    }
}
