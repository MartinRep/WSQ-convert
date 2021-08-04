package wsq_convert;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Runner {

	public static void main(String[] args) {
		WsqConvert worker = new WsqConvert();
		BufferedImage image;
		File outputFile = new File("/home/martinrep/projects/fingers/saved3.png");
		String inputFile = "/home/martinrep/projects/fingers/3.wsq";
		try {
			image = worker.readImage(inputFile);
			ImageIO.write(image, "png", outputFile);
			List<String> processedFiles = worker.convertImages(new File("/home/martinrep/projects/fingers-test"));
			System.out.printf("Total files processed: %d", processedFiles.size());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
