import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MemoService {
    private static final File memoFolder = new File("memos");
    private static final Scanner scanner = new Scanner(System.in);

    // 1. 메모 작성
    public static void createMemo() {

        // 메모가 저장될 폴더가 없으면 생성
        if (!memoFolder.exists()) {
            boolean createdFolder = memoFolder.mkdirs();
            if (!createdFolder && !memoFolder.exists()) {
                System.out.println("폴더 생성에 실패했습니다.");
            }
        }

        // 메모 내용 입력 및 저장될 이름 입력
        System.out.println("메모 내용을 입력하세요.(:wq 입력 시 저장) : ");

        // 여러 줄 입력 가능
        StringBuilder content = new StringBuilder();

        // 여러 줄 입력 후 :wq 입력해야 저장
        while (true) {
            String line = scanner.nextLine();

            if (line.equals(":wq")) {
                break;
            }
            content.append(line).append(System.lineSeparator());
        }

        // 내용이 없으면 저장 안되게 막음
        if (content.length() == 0) {
            System.out.println("메모 내용이 없습니다. 저장하지 않겠습니다.");
            return;
        }

        System.out.println("저장할 이름을 입력하세요 : ");
        String title = scanner.nextLine();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName = title + "_" + timestamp + ".txt";
        File file = new File(memoFolder, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content.toString());
            System.out.println("메모 저장 완료 : " + fileName);
        } catch (IOException e) {
            System.out.println("메모 저장 실패 : " + e.getMessage());
        }
    }

    // 2. 메모 검색
    public static void searchMemo() {
        System.out.println("검색할 단어를 입력하세요. : ");
        String keyword = scanner.nextLine().toLowerCase();

        File[] memoFiles = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (memoFiles == null || memoFiles.length == 0) {
            System.out.println("검색할 메모 파일이 없습니다.");
            return;
        }

        boolean found = false;

        for (File file : memoFiles) {
            if (searchInFile(file, keyword)) {
                found = true;
            }
        }

        if (!found) {
            System.out.println("해당 단어가 포함된 메모를 찾을 수 없습니다.");
        }
    }

    private static boolean searchInFile(File file, String keyword) {
        boolean found = false;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    if (!found) {
                        System.out.println("\n" + file.getName() + " 에서 발견됨 : ");
                    }
                    System.out.printf("    %2d: %s%n", lineNum, line);
                    found = true;
                }
                lineNum++;
            }
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + file.getName());
        }
        return found;
    }

    // 3. 메모 리스트
    public static void listMemos() {
        List<File> memoFiles = getMemoFiles();

        if (memoFiles.isEmpty()) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        System.out.println("저장된 메모 목록 : ");
        for (int i = 0; i < memoFiles.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, memoFiles.get(i).getName());
        }
    }

    private static List<File> getMemoFiles() {
        File[] files = memoFolder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return new ArrayList<>();

        return Arrays.stream(files)
                     .sorted(Comparator.comparing(File::getName).reversed())
                     .collect(Collectors.toList());
    }

    // 4. 메모 보기
    public static void viewMemo() {
        List<File> memoFiles = getMemoFiles();

        if (memoFiles.isEmpty()) {
            System.out.println("저장된 메모가 없습니다.");
            return;
        }

        listMemos();

        System.out.println("열람할 메모 번호를 입력하세요.: ");
        int index = readUserIndex(memoFiles.size());
        if (index == -1) return;

        File selectedFile = memoFiles.get(index -1);
        displayFileContent(selectedFile);
    }

    private static int readUserIndex(int max) {
        try {
            int index = Integer.parseInt(scanner.nextLine());

            if (index < 1 || index > max) {
                System.out.println("잘못된 번호입니다.");
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요. : ");
            return -1;
        }
    }

    private static void displayFileContent(File file) {
        System.out.printf("메모 내용 (" + file.getName() + ") : ");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }

    // 5. 메모 삭제
    public static void deleteMemo() {
        List<File> memoFiles = getMemoFiles();

        if (memoFiles.isEmpty()) {
            System.out.println("삭제할 메모가 없습니다.");
            return;
        }

        listMemos();
        System.out.println("삭제할 메모 번호를 입력하세요. : ");
        int index = readUserIndex(memoFiles.size());
        if (index == -1) return;

        File fileToDelete = memoFiles.get(index -1);

        if (fileToDelete.delete()) {
            System.out.println(fileToDelete.getName() + "_메모가 삭제되었습니다.");
        } else {
            System.out.println(fileToDelete.getName() + "_삭제에 실패했습니다.");
        }
    }
}
