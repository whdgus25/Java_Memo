import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MemoApp_backup {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        File memoFolder = new File("./memo");

        // 메모 저장 폴더 없으면 생성
        // if (!memoFolder.exists()) { memoFolder.mkdir();}
        memoFolder.mkdirs();


        // 프로그램이 종료될 때까지 반봅
        while (true) {
            System.out.println("\n 메모장 프로그램");
            System.out.println("1. 메모 작성");
            System.out.println("2. 메모 목록");
            System.out.println("3. 메모 열기");
            System.out.println("4. 메모 수정");
            System.out.println("5. 메모 삭제");
            System.out.println("6. 메모 검색");
            System.out.println("메뉴 선택 : ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요!");
                continue;
            }

            switch (choice) {
                case 1 -> writeNewMemo(scanner, memoFolder);
                case 2 -> showMemoList(memoFolder);
                case 3 -> openMemo(scanner, memoFolder);
                case 4 -> editeMemo(scanner, memoFolder);
                case 5 -> deleteMemo(scanner, memoFolder);
                case 6 -> searchMemo(scanner, memoFolder);
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 1번 기능
    private static void writeNewMemo(Scanner scanner, File memoFolder) {
        System.out.println("\n 새 메모를 입력하세요. (입력 끝내려면 빈 줄 :wq)");

        StringBuilder memoContent = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();

            if (line.equals(":wq")) {
                break;
            }
            memoContent.append(line).append(System.lineSeparator());
        }

        if (memoContent.length() == 0) {
            System.out.println("메모 내용이 없습니다. 저장하지 않습니다.");
            return;
        }

        System.out.println("저장될 이름을 입력하세요.");
        String title = scanner.nextLine();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName = "메모_"+title +"_"+timestamp + ".txt";
        File memoFile = new File(memoFolder, fileName);

        try (FileWriter writer = new FileWriter(memoFile)) {
            writer.write(memoContent.toString());
            System.out.println("메모가 저장되었습니다." + memoFile.getPath());
        } catch (IOException e) {
            System.out.println("메모 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 2번 기능
    private static void showMemoList(File memoFolder) {
        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        System.out.println("\n 저장된 메모 목록 : ");
        for (int i = 0; i < memoFiles.length; i++) {
            System.out.println((i + 1) + ". " + memoFiles[i].getName());
        }
    }

    // 3번 기능
    private static void openMemo(Scanner scanner, File memoFolder) {
        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        System.out.println("\n 저장된 메모 목록");
        for (int i = 0; i < memoFiles.length; i++) {
            System.out.println((i + 1) + ". " + memoFiles[i].getName());
        }

        System.out.println("열어볼 메모 번호를 입력하세요.");

        int index;

        try {
            index = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        if (index < 1 || index > memoFolder.length()) {
            System.out.println("유효한 번호를 입럭하세요!");
            return;
        }

        File selectedFile = memoFiles[index -1];
        System.out.println("\n 메모 내용 ("+ selectedFile.getName() + "):");

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("파일 읽기 오류 : " + e.getMessage());
        }
    }

    //4번 기능
    private static void editeMemo(Scanner scanner, File memoFolder) {
        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        // 목록 출력
        System.out.println("\n저장된 메모 목록");
        for (int i = 0; i < memoFiles.length; i++) {
            System.out.println((i + 1) + ". " + memoFiles[i].getName());
        }

        System.out.println("수정할 메모 번호를 입력하세요.");
        int index;

        try {
            index = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e) {
            System.out.println("숫자를 입력하세요!");
            return;
        }

        if (index < 1 || index > memoFiles.length) {
            System.out.println("유효한 번호를 입력하세요!");
            return;
        }

        File selectedFile = memoFiles[index - 1];

        // 기존 내용 출력
        System.out.println("\n 현재 메모 내용: ");
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("파일 읽기 오류 : " + e.getMessage());
            return;
        }

        // 새 내용 입력
        System.out.println("\n 수정할 내용을 입력하세요 : ");
        StringBuilder newContent = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals(":wq")) {
                break;
            }
            newContent.append(line).append(System.lineSeparator());
        }

        if (newContent.length() == 0) {
            System.out.println("수정할 내용이 없습니다. 취소합니다.");
            return;
        }

        // 파일 덮어쓰기
        try (FileWriter writer = new FileWriter(selectedFile)) {
            writer.write(newContent.toString());
            System.out.println("메모가 수정되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생 : " + e.getMessage());
        }
    }

    // 5번 기능
    private static void deleteMemo(Scanner scanner, File memoFolder) {
        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        // 목록 출력
        System.out.println("\n 저장된 메모 목록 : ");
        for (int i = 0; i < memoFiles.length; i++) {
            System.out.println((i + 1) + ". " + memoFiles[i].getName());
        }

        System.out.println("삭제할 메모 번호를 입력하세요.");
        int index;
        try {
            index = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력하세요!");
            return;
        }

        if (index < 1 || index > memoFiles.length) {
            System.out.println("유효한 번호를 입력하세요!");
            return;
        }

        File selectedFile = memoFiles [index - 1];

        // 파일 삭제
        if (selectedFile.delete()) {
            System.out.println("메모가 삭제되었습니다." + selectedFile.getName());
        } else {
            System.out.println("메모 삭제 중 오류 박생.");
        }
    }

    // 6번 기능
    private static void searchMemo(Scanner scanner, File memoFolder) {
        System.out.println("\n 검색할 키워드를 입력하세요 : ");

        String keyword = scanner.nextLine();

        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        boolean found = false;

        System.out.println("\n 검색 결과 : ");
        for (File file : memoFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();

                String line;
                boolean matched = false;

                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                    if (line.contains(keyword)) {
                        matched = true;
                    }
                }

                if (matched) {
                    found = true;
                    System.out.println("파일 : " + file.getName());
                    System.out.println("내용 일부 : \n" + content.toString().substring(0, Math.min(100, content.length())));
                    System.out.println("-----------------------------------------------------");
                }

            } catch (IOException e) {
                System.out.println("파일 읽기 오류 : " + file.getName());
            }
        }
        if (!found) {
            System.out.println("해당 키워드가 포함한 메모가 없습니다.");
        }
    }

}
