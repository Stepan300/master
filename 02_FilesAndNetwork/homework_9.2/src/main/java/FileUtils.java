import java.io.*;

public class FileUtils {

    public static void copyFolder(String sourceDirectory, String destinationDirectory)
            throws SecurityException
    {
        try {
            File sourceFile = new File(sourceDirectory);
            File destinationFile = new File(destinationDirectory);

            if (sourceFile.isDirectory()) {

                if (!destinationFile.exists()) {
                    destinationFile.mkdir();
                }

                File[] files = sourceFile.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String desNameDirNew = destinationDirectory + "/" + file.getName();
                        copyFolder(file.getAbsolutePath(), desNameDirNew);
                    }
                }
                else {
                    throw new NullPointerException();
                }
            }

            if (sourceFile.isFile()) {
                copyFile(sourceFile, destinationFile);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private static void copyFile(File sourceFile, File destinationFile) throws Exception
    {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            if (destinationFile.exists()) {
                System.out.printf("%s%-80s%s%n", "File: ", destinationFile.getPath(),
                        " - had copied successfully");
            }
            else {
                System.out.println("Something is wrong!");
                throw new Exception();
            }
        } catch (IOException ex) {ex.printStackTrace();}

        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {ex.printStackTrace();}
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {ex.printStackTrace();}
            }
        }
    }
}