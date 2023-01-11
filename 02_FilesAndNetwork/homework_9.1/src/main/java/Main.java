import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Scanner;

public class Main {

    public static String sumSizeForm;

    public static void main(String[] args) throws NullPointerException {
        System.out.println("Enter directory: ");
        Scanner sc = new Scanner(System.in);
        for (;;) {
            String path = sc.nextLine().trim();
            File folder = new File(path);
            try {
                if (!folder.exists()) {
                    throw new FileNotFoundException("File isn't found," +
                            " try to fix the error in the address");
                }
                sumSizeForm = FileUtils.calculateFolderSize(path);
                System.out.println("=================================================================");
                System.out.println("Total Size: " + sumSizeForm);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
