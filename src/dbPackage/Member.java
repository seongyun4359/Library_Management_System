package dbPackage;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Member {
	private String memberId;
	private String name;
	private String password;
	private String memberGrade;

	public Member(String memberId, String name, String password, String memberGrade) {
		this.memberId = memberId;
		this.name = name;
		this.password = password;
		this.memberGrade = memberGrade;

	}

	public void registerMember(Connection conn) {
		// 유효성 검사
		Scanner scanner = new Scanner(System.in);
		String id = null;
		String pw = null;
		String name;
		String phone;

		// ====회원가입====
		System.out.println("\n=======================================");
		System.out.println("\n           ★ 티니핑 대도서관 ★         \n");
		System.out.println("=======================================\n");

		// ID 입력 및 유효성 검사
		while (true) {
			System.out.print("\nID 를 입력하세요 (영문 소문자, 숫자 포함 5~10글자): ");
			id = scanner.nextLine();

			// ID 유효성 검사 (정규식)
			if (!id.matches("[a-z0-9]{5,10}")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				continue;
			}
			// DB에서 ID 중복 확인
			if (isDuplicateID(id, conn)) { // isDuplicate 라는 내부 메서드 실행
				System.out.println("중복된 ID 입니다. 다시 입력해주세요.\n");
				continue;
			}

			break; // 유효성 검사 및 중복 확인을 통과하면 탈출
		}

		// 비밀번호 입력 및 유효성 검사
		while (true) {
			System.out.print("\n비밀번호 (길이 8 이상, 숫자 포함): ");
			pw = scanner.nextLine();

			// 비밀번호 강도 검사 (길이 8 이상, 숫자 포함)
			if (!pw.matches("^(?=.*[0-9]).{8,}$")) {
				System.out.println("비밀번호가 너무 약합니다. 다시 입력해주세요.");
				continue;
			}

			// 비밀번호 확인
			System.out.print("비밀번호 확인: ");
			String pwConfirm = scanner.nextLine();

			if (!pw.equals(pwConfirm)) {
				System.out.println("두 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				continue;
			}

			break; // 비밀번호가 유효하면 탈출
		}

		// 이름 입력
		System.out.print("\n이름을 입력하세요: ");
		name = scanner.nextLine();

		// 전화번호 입력 및 유효성 검사
		while (true) {
			System.out.print("\n전화번호를 입력하세요 (숫자 11자리): ");
			phone = scanner.nextLine();

			// 전화번호가 11자리 숫자인지 확인
			if (!phone.matches("\\d{11}")) {
				System.out.println("잘못된 전화 번호 입니다. 다시 입력해주세요.");
				continue;
			}

			break; // 전화번호가 유효하면 탈출
		}

		// 회원 등록
		String insertQuery = "INSERT INTO usertbl (name, memberGrade, ID, PW, phone) VALUES (?, '일반', ?, ?, ?)";
		try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
			insertStmt.setString(1, name);
			insertStmt.setString(2, id);
			insertStmt.setString(3, pw);
			insertStmt.setString(4, phone);
			insertStmt.executeUpdate();
			Menu.ConsoleUtil.clearConsole();
			System.out.println("\n회원가입이 완료되었습니다.\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean isDuplicateID(String pid,Connection conn) {
        boolean isDuplicate = false;
        
            String query = "SELECT COUNT(*) FROM UserTbl WHERE ID = ?";
              try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                  pstmt.setString(1, pid);
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                      isDuplicate = rs.getInt(1) > 0;
                  
                  }
              }catch (SQLException e) {
      			e.printStackTrace();
      		}
                    
        return isDuplicate;
	}

	public static Member login(Connection conn, String inputId, String inputPw) {
        // 로그인 로직 (DB에서 사용자 정보 확인)
        Member member = null;
        String query = "SELECT memberId, name, memberGrade FROM usertbl WHERE ID = ? AND PW = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, inputId);
            stmt.setString(2, inputPw);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String memberId = rs.getString("memberId");
                String name = rs.getString("name");
                String memberGrade = rs.getString("memberGrade");
                member = new Member(memberId, name, inputPw, memberGrade); // 로그인 성공 시 Member 객체 생성
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

//    public void searchBook(Connection conn) {
//        // 검색 도서 로직 (가정)
//    }

	public void viewMemberInfo() {
        System.out.println("회원 ID: " + this.memberId);
        System.out.println("회원 이름: " + this.name);
        System.out.println("회원 등급: " + this.memberGrade);
    }

	public void extendRentalPeriod(Connection conn) {
        System.out.println("대여 연장 요청이 완료되었습니다."); // 실제 로직은 추가 필요
    }

	// Getter 메서드
	public String getMemberId() {
        return memberId;
    }

	public String getName() {
        return name;
    }

	public String getMemberGrade() {
        return memberGrade;
    }
}
