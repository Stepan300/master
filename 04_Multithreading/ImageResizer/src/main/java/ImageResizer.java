import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.io.File;

public class ImageResizer extends Thread
{
    private final File[] files;
    private final int newWidth;
    private final String dstFolder;
    private final long start;
    private final int threadCount;
 // =====================================================================

    public ImageResizer(File[] files, int newWidth, String dstFolder, long start, int threadCount) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
        this.threadCount = threadCount;
    }

    @Override
    public void run() {
        int imageCount = 0;
        try {
            assert files != null;
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }
                int newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));

                BufferedImage newImage = resize(image, newWidth, newHeight, Scalr.OP_ANTIALIAS);
                image.flush();

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
                newImage.flush();
                imageCount++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.printf("%-12s%2d%11s%7d%8s%4d%18s%n", "Duration of", threadCount, "thread is",
                (System.currentTimeMillis() - start), "ms. for", imageCount, "resizable images");
    }

    public static BufferedImage resize(BufferedImage image, int targetWidth, int targetHeight,
                  BufferedImageOp... ops) throws IllegalArgumentException, ImagingOpException {
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight,
                ops);
    }
}
