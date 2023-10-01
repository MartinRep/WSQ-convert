package wsq_convert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnbis.Bitmap;
import org.jnbis.WSQDecoder;

public class WsqConvert {

    public static BufferedImage bitmapToBufferedImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] data = bitmap.getPixels();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, width, height, data);

        return image;
    }

    public static BufferedImage readBitmapImage(String fileName) throws IOException {
        FileInputStream stream = new FileInputStream(new File(fileName));
        Bitmap bitmap = WSQDecoder.decode(stream);
        return bitmapToBufferedImage(bitmap);
    }

    public static List<String> fetchFilePathsMatchingPattern(final String pattern, final File folder) {
        List<String> filePaths = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                filePaths.addAll(fetchFilePathsMatchingPattern(pattern, file));
            }
            if (file.isFile() && file.getName().matches(pattern)) {
                filePaths.add(file.getAbsolutePath());
            }
        }
        return filePaths;
    }

    public static List<BufferedImage> readMultipleImages(final File folder) throws IOException {
        List<String> filePaths = fetchFilePathsMatchingPattern(".*\\.wsq", folder);
        List<BufferedImage> images = new ArrayList<>();

        for (String filePath : filePaths) {
            images.add(readBitmapImage(filePath));
        }

        return images;
    }

    public static List<String> convertImagesToPng(final File folder) throws IOException {
        List<String> convertedFiles = new ArrayList<>();
        List<String> filePaths = fetchFilePathsMatchingPattern(".*\\.wsq", folder);

        for (String filePath : filePaths) {
            BufferedImage image = readBitmapImage(filePath);
            String outputFilePath = filePath.replaceAll("\\.wsq$", ".png");
            File outputFile = new File(outputFilePath);
            ImageIO.write(image, "png", outputFile);
            convertedFiles.add(outputFilePath);
        }

        return convertedFiles;
    }
}
