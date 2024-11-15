package dbPackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
	private static Scanner scanner = new Scanner(System.in);
	private Member member = null;
	private Member currentUser = null;
	private Admin admin = new Admin();

	// DBConnection 객체를 통해 연결을 가져옴
	private Connection conn = null;

	public Menu() {
		try {
			conn = DBConnection.getConnection(); // DB 연결 초기화
		} catch (SQLException e) {
			System.out.println("DB 연결 실패: " + e.getMessage());
		}
	}

	public class ConsoleUtil {

	    // 콘솔을 초기화하는 메소드 (빈 줄 50개 출력)
	    public static void clearConsole() {
	        for (int i = 0; i < 70; i++) {
	            System.out.println();
	        }
	    }
	}
	
	public void JoinTitle() {
		System.out.println("================================ 티니핑 대도서관 ================================");
		System.out.println("");
		System.out.println("	   ／＞　 フ		 .---. .-..-. .-..-..----. .-..-. .-. .---. ");
		System.out.println("	  | 　_　_|		{_   _}| ||  `| || || {}  }| ||  `| |/   __} ");
		System.out.println("	／` ミ＿x ノ		  | |  | || |\\| || || |-'' | || |`_ |   \\");
		System.out.println("	/　　　　 |		  `-'  `-'`-' `-'`-'`-'    `-'`-' `-' `---'  ");
		System.out.println("	│　　  |　|			");
		System.out.println("	／￣|　|　|　|		.-.   .-..----. .----.   .--.   .----. .-.  .-.");
		System.out.println("	＼　|　|　|　|		| |   | || {}  }| {}  } / {} \\ | {}  }  \\ \\/ / ");
		System.out.println("	  `￣|　|　|		| `--.| || {}  }| .-.\\/   /\\ \\ | .-. \\	 }  {  ");
		System.out.println("	      `ー'`ー'		`----'`-'`----' `-' `-' `-'  `-' `-'-'	 `--'    ");
		System.out.println("");

	}

	// 메인메뉴
	public void MainMenu() throws SQLException {
		int main_Menu_Choice = 0;

		while (true) {
			System.out.println("=======================================");
			System.out.println("");
			System.out.println("           ★ 티니핑 대도서관 ★         ");
			System.out.println("");
			System.out.println("=======================================");
			System.out.println("");
			System.out.println("	- 접속 경로를 선택해주세요. -");
			System.out.println("");
			System.out.println("	  1. 로그인(Login)");
			System.out.println("	  2. 회원가입(Register)");
			System.out.println("	  3. 종료(Exit)");
			System.out.println("");
			System.out.println("=======================================");
			System.out.println("");
			System.out.print("숫자를 입력해주세요: ");

			if (scanner.hasNextInt()) {
				main_Menu_Choice = scanner.nextInt();
				scanner.nextLine(); // 줄바꿈 제거
				Menu.ConsoleUtil.clearConsole();

				switch (main_Menu_Choice) {
				case 1:
					currentUser = loginMenu(conn); // 로그인 메뉴 호출
					if (currentUser != null) {
						ConsoleUtil.clearConsole();
						System.out.println("로그인 성공: " + currentUser.getName() + "님, 환영합니다.");
						if (currentUser.getMemberGrade().equals("관리자")) {
							adminMenu(admin, conn); // 관리자 메뉴 호출
						} else if (currentUser.getMemberGrade().equals("퇴출")) {
							System.out.println("해당 계정은 퇴출된 계정입니다. 이용하실 수 없습니다!");
						} else {
							userMenu(currentUser, conn); // 일반 사용자 메뉴 호출
						}
					} else {
						System.out.println("로그인 실패: 잘못된 ID 또는 비밀번호입니다!");
					}
					break;

				case 2:
					Member newMember = new Member(null, null, null, null);
					newMember.registerMember(conn); // 회원 가입
					break;
				case 3:
					System.out.println("");
					System.out.print("정말로 종료하시겠습니까? (Y/N): ");
					String exitChoice = scanner.nextLine().trim().toUpperCase();
					if (exitChoice.equals("Y")) {
						System.out.println("\n프로그램이 종료되었습니다.");
						DBConnection.closeConnection(); // DB 연결 종료
						System.exit(0);
					} else if (exitChoice.equals("N")) {
						System.out.println("접속 경로를 다시 선택해주세요.");
					} else {
						System.out.println("잘못된 선택입니다. 메뉴로 돌아갑니다.");
					}
					break;
				default:
					System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
				}
			} else {
				System.out.println("선택지(숫자)를 입력해주세요.");
				scanner.next(); // 잘못된 입력 처리
				continue; // 다시 입력을 받기 위해 루프 재시작
			}
		}
	}

	// 로그인 메뉴
	public static Member loginMenu(Connection conn) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("");
		System.out.print("ID를 입력하세요: ");
		String inputId = scanner.nextLine();
		System.out.print("비밀번호를 입력하세요: ");
		String inputPw = scanner.nextLine();
		System.out.println("");
		return Member.login(conn, inputId, inputPw); // 로그인 시 데이터베이스에서 확인
	}

	// 사용자 메뉴
	public static void userMenu(Member member, Connection conn) throws SQLException {
	    while (true) {
	        System.out.println("\n=======================================");
	        System.out.println("\n      - 사용자 메뉴를 선택해주세요. -\n");
	        System.out.println("=======================================\n");
	        System.out.println("	    1. 도서 검색");
	        System.out.println("	    2. 도서 대여");
	        System.out.println("	    3. 도서 반납");
	        System.out.println("	    4. 회원 정보 보기");
	        System.out.println("	    5. 로그아웃");
	        System.out.println("\n=======================================\n");
	        System.out.print("숫자를 입력해주세요: ");

	        String input = scanner.nextLine(); // 입력을 문자열로 받기
	        ConsoleUtil.clearConsole();

	        try {
	            int choice = Integer.parseInt(input); // 입력을 정수로 변환

	            switch (choice) {
	                case 1:
	                    Book.searchBook(conn); // 도서 검색
	                    break;
	                case 2:
	                    Book.rentBook(member, conn); // 도서 대여
	                    break;
	                case 3:
	                    Book.returnBook(member, conn); // 도서 반납
	                    break;
	                case 4:
	                    member.viewMemberInfo(); // 회원 정보 보기
	                    break;
	                case 5:
	                    System.out.println("로그아웃 완료.");
	                    return;
	                default:
	                    System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
	        }
	    }
	}


	// 관리자 메뉴
	public static void adminMenu(Admin admin, Connection conn) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			
	        System.out.println("\n=======================================");
	        System.out.println("\n      - 관리자 메뉴를 선택해주세요. -\n");
	        System.out.println("=======================================\n");
	        System.out.println("	   1. 전체 회원 관리");
	        System.out.println("	   2. 도서 목록 관리");
	        System.out.println("	   3. 로그아웃");
	        System.out.println("\n=======================================\n");
	        System.out.print("숫자를 입력해주세요: ");

			if (scanner.hasNextInt()) {
				int choice = scanner.nextInt();
				scanner.nextLine(); // 줄바꿈 제거
				switch (choice) {
				case 1:
					ConsoleUtil.clearConsole();
					admin.manageUsers(conn); // 회원 관리
					break;
				case 2:
					Menu.ConsoleUtil.clearConsole();
					admin.manageBooks(conn); // 도서 관리
					break;
				case 3:
					Menu.ConsoleUtil.clearConsole();
					System.out.println("관리자 로그아웃 완료.");
					return;
				default:
					Menu.ConsoleUtil.clearConsole();
					System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
				}
			} else {
				System.out.println("숫자를 입력해주세요.");
				scanner.next(); // 잘못된 입력 처리
			}
		}
	}
}