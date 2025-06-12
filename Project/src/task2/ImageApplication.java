package task2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageApplication {

	public static int level = 255;
	public static int num_loops = 5;

	public static void main(String[] args) {
		String fileName1 = "c:/image/Rain_Tree.jpg";
		String fileName2 = "c:/image/Wr.jpg";

		colourImage input_img = new colourImage();
		imageReadWrite.readJpgImage(fileName1, input_img);

		for (int i = 0; i < num_loops; i++) {
			// 1.1 create an empty output image
			colourImage output = new colourImage();

			// 1.2 get height & width
			output.width = input_img.width;
			output.height = input_img.height;

			// 1.3 create the pixel values
			output.pixels = new short[output.height][output.width][3];

			// 1.4 call histogram equalization
			SingleHistogramEqualization(input_img, output);

			// 1.5 write the image
			imageReadWrite.writeJpgImage(output, fileName2);
		}
	}

	public static void SingleHistogramEqualization(colourImage input, colourImage output) {
		int size = input.height * input.width;

		for (int color = 0; color < 3; color++) {
			int[] histogram = new int[level + 1];

			// step 1: compute the histogram
			for (int i = 0; i < input.height; i++) {
				for (int j = 0; j < input.width; j++) {
					histogram[input.pixels[i][j][color]]++;
				}
			}

			// step 2: calculate the cumulative histogram
			int[] cumulativeHist = new int[level + 1];
			cumulativeHist[0] = histogram[0];
			for (int i = 1; i <= level; i++) {
				cumulativeHist[i] = cumulativeHist[i - 1] + histogram[i];
			}

			// step 3: (to be completed - normalize cumulative hist)
			// output mapping logic will be added next
		}
	}
}

class imageReadWrite {
	public static void readJpgImage(String fileName, colourImage ImgStruct) {
		try {
			File file = new File(fileName);
			BufferedImage image = ImageIO.read(file);

			System.out.println("file: " + file.getCanonicalPath());

			if (!image.getColorModel().getColorSpace().isCS_sRGB()) {
				System.out.println("Image is not in sRGB color space");
				return;
			}

			int width = image.getWidth();
			int height = image.getHeight();
			ImgStruct.width = width;
			ImgStruct.height = height;
			ImgStruct.pixels = new short[height][width][3];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					Color color = new Color(pixel, true);
					ImgStruct.pixels[y][x][0] = (short) color.getRed();
					ImgStruct.pixels[y][x][1] = (short) color.getGreen();
					ImgStruct.pixels[y][x][2] = (short) color.getBlue();
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading image file: " + e.getMessage());
		}
	}

	public static void writeJpgImage(colourImage ImgStruct, String fileName) {
		try {
			int width = ImgStruct.width;
			int height = ImgStruct.height;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int rgb = new Color(
						ImgStruct.pixels[y][x][0],
						ImgStruct.pixels[y][x][1],
						ImgStruct.pixels[y][x][2]
					).getRGB();
					image.setRGB(x, y, rgb);
				}
			}

			File outputFile = new File(fileName);
			ImageIO.write(image, "jpg", outputFile);
		} catch (IOException e) {
			System.out.println("Error writing image file: " + e.getMessage());
		}
	}
}

class colourImage {
	public int width;
	public int height;
	public short[][][] pixels;
}
