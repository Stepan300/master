import java.io.File;

public class Main {

    private static final int newWidth = 300;
    private static final String srcFolder = "C:\\Users\\Mike\\Desktop\\src";
    private static final String dstFolder = "C:\\Users\\Mike\\Desktop\\dst";

    public static void main(String[] args) {

        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Cores: " + cores);

        assert files != null;
        int photosInThread = files.length / cores;
        int remainder = files.length % cores;
        int addition = 1;
        int multiplier = 1;
        int srcPos = 0;

        long start = System.currentTimeMillis();

        for (int j = 1; j <= cores; j++) {
            if (remainder == 0) {
                addition = 0;
            }
            try {
                File[] filesImage = new File[photosInThread + addition];
                System.arraycopy(files, srcPos, filesImage, 0, filesImage.length);
                ImageResizer resizer = new ImageResizer(filesImage, newWidth, dstFolder, start, j);
                resizer.start();
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
            srcPos = multiplier++ * photosInThread + addition;
            if (remainder != 0) {
                remainder--;
            }
        }
    }
}