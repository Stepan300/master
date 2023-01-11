import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String destinationDirectory = "files/";

    public static void main(String[] args) {

        System.out.println("Enter directory to copy:");
        Scanner sc = new Scanner(System.in);
        for (;;) {
            String path = sc.nextLine().trim();
            try {
                File sourceFile = new File(path);
                if (!sourceFile.exists()) {
                    throw new FileNotFoundException("File isn't found, try to fix the error" +
                            " in the address");
                }
                String desNameDir = destinationDirectory + sourceFile.getName();
                FileUtils.copyFolder(path, desNameDir);
                System.out.println("=============================================================");
                System.out.println("Well done! ");
            } catch (Exception  ex) {
                ex.printStackTrace();
            }
        }
    }
}
