package wsq_convert;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jnbis.Bitmap;
import org.jnbis.WSQDecoder;



class WsqConvert {

	private static BufferedImage convert(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		byte[] data = bitmap.getPixels();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		raster.setDataElements(0, 0, width, height, data);
		return image;
	}
	
	
	public BufferedImage readImage(String fileName) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		Bitmap bitmap = WSQDecoder.decode(new FileInputStream(file));
		BufferedImage image = convert(bitmap);
		return image;
		
	}
	
	public static void traverseDirectory(final String pattern, final File folder, List<String> fileNamesList) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                traverseDirectory(pattern, f, fileNamesList);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    fileNamesList.add(f.getAbsolutePath());
                }
            }
        }
	}
	
	public List<BufferedImage> readImages(final File folder) throws FileNotFoundException, IOException{
		List<String> fileNamesList = new ArrayList<String>();
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		traverseDirectory(".*\\.wsq", folder, fileNamesList);
		for (String fileName : fileNamesList) {
			images.add(readImage(fileName));
		}
		return images;
	}
	
	
	public List<String> convertImages(final File folder) throws IOException{
		List<String> fileNamesList = new ArrayList<String>();
		traverseDirectory(".*\\.wsq", folder, fileNamesList);
		for (String fileName : fileNamesList) {
			BufferedImage image = readImage(fileName);
			int pos = fileName.lastIndexOf(".");
			if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
			    fileName = fileName.substring(0, pos);
			    fileName = fileName.concat(".png");
				File outputFile = new File(fileName);
				ImageIO.write(image, "png", outputFile);
//				System.out.printf("File: %s was saved.\n", outputFile);	
			}
		}
		return fileNamesList;
	}
}
