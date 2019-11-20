package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileManager {

	// method that saves a buffered image to a directory
	public static void exportFile(BufferedImage outputImage, String directory) {

		// load a file from the directory in the directory and see if it exist
		File outputFile = new File(directory);

		try {

			// if the directory exist, save the output image as a jpg
			ImageIO.write(outputImage, "txt", outputFile);

		} catch (IOException error) {

			System.out.println("no directory found with given name");

		}

	}

}
