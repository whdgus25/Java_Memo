import java.util.Scanner;

public class MemoApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n 메모장 프로그램");
            System.out.println("1. 메모 작성");
            System.out.println("2. 메모 검색");
            System.out.println("3. 메모 리스트");
            System.out.println("4. 메모 보기");
            System.out.println("5. 메모 삭제");
            System.out.println("0. 종료");
            System.out.println("번호 선택 : ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> MemoService.createMemo();
                case "2" -> MemoService.searchMemo();
                case "3" -> MemoService.listMemos();
                case "4" -> MemoService.viewMemo();
                case "5" -> MemoService.deleteMemo();
                case "0" -> {
                    System.out.println("종료합니다.");
                    return;
                }
                default -> System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}

