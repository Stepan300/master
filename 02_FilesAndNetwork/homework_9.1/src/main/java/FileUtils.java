import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static long sumSize;

    public static String calculateFolderSize(String path) throws NullPointerException, SecurityException {
        sumSize = 0L;
        File folder = new File(path);
        try {
            if (folder.isFile()) {
                sumSize += folder.length();
                System.out.printf("%-75s%30s%n", folder.getAbsolutePath(), reductionSize(folder.length()));
            }
            if (folder.isDirectory()) {
                sumSize += folderSize(folder);
            } else {
                throw new NullPointerException("Array files is null! ");
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return reductionSize(sumSize);
    }

    private static long folderSize(File directory) {
        sumSize = 0L;
        File[] files = directory.listFiles();
        try {
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        sumSize += file.length();
                        System.out.printf("%-75s%30s%n", file.getAbsolutePath(), reductionSize(file.length()));
                    } else {
                        sumSize += folderSize(file);
                    }
                }
            }
            else {
                throw new NullPointerException();
            }
        } catch (SecurityException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return sumSize;
    }

    private static String reductionSize(Long bytes) {
        String bytesToStr = bytes.toString();
        int numberOfDigits = bytesToStr.length();
        if (numberOfDigits <= 3) {
            return bytes + " Bt";
        }
        if (numberOfDigits <= 6) {
            return (double) bytes/1024 + " Kb";
        }
        if (numberOfDigits <= 9) {
            return (double) bytes/(1024*1024) + " Mb";
        }
        if (numberOfDigits <= 12) {
            return (double) bytes/(1024*1024*1024) + " Gb";
        }
        return null;
    }
}