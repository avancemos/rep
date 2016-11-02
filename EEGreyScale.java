package picClass;

/**
 * Contains and tests copyPic, greyScale, scale, crop methods
 * 
 * @author Emmanuel (EJ, Asiago, Jesus) Eppinger
 */
public class EEGreyScale {

	/**
	 * Tests scale, greyScale, copy, and crop methods
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// File Path
		String filePath = args[0];

		// p is picture at file pat
		Picture p = new Picture(filePath);

		// scales picture to one 20th
		Picture scaled = scale(p, -20.0);

		// Rescaled to 20x
		scaled = scale(scaled, 20.0);

		// makes greyScale of origional
		Picture grey = greyScale(p);

		// scales scale to half
		scaled = scale(scaled, -2.0);

		// combines scaled and grey into one image
		Picture combo = copyPic(grey, scaled, 10, 20);

		// displays combo
		combo.display();

		// crops combo
		combo = crop(combo, 10, 20, 400, 100);

		// redisplays combo
		combo.display();

	}

	/**
	 * Takes addedImage image onto baseImage
	 * 
	 * @param baseImage
	 *            Base image to have another image copied onto
	 * @param addedImage
	 *            Image to be copied on top of baseImage
	 * @param xStart
	 *            x coordinate of top left corner of addedImage on baseImage
	 * @param yStart
	 *            y coordinate of top left corner of addedImage on baseImage
	 * @return Image that is composite of addedImage with top left corner
	 *         (xStart, yStart) with baseImage as all other pixels
	 */
	public static Picture copyPic(Picture baseImage, Picture addedImage, int xStart, int yStart) {

		// Creates output image with same height and width of baseImage
		Picture outputImage = new Picture((int) (baseImage.getWidth()), (int) (baseImage.getHeight()));

		// Copies baseImage onto outputImage
		for (int x = 0; x < baseImage.getWidth(); x++) {
			for (int y = 0; y < baseImage.getHeight(); y++) {

				// Sets outputImage at x,y to baseImage at x,y
				outputImage.setPixelRed(x, y, baseImage.getPixelRed(x, y));
				outputImage.setPixelGreen(x, y, baseImage.getPixelGreen(x, y));
				outputImage.setPixelBlue(x, y, baseImage.getPixelBlue(x, y));

			}
		}

		// loops through coping addedImage to outputImage with top left corner
		// at xStart, yStart
		for (int x = xStart; x < xStart + addedImage.getWidth(); x++) {
			for (int y = yStart; y < yStart + addedImage.getHeight(); y++) {

				outputImage.setPixelRed(x, y, addedImage.getPixelRed(x - xStart, y - yStart));
				outputImage.setPixelGreen(x, y, addedImage.getPixelGreen(x - xStart, y - yStart));
				outputImage.setPixelBlue(x, y, addedImage.getPixelBlue(x - xStart, y - yStart));

			}
		}

		// return outputImage
		return outputImage;

	}

	/**
	 * Returns image that is grey-scaled
	 * 
	 * @param coloredImage
	 *            Image to be made into greyScale
	 * @return Copy of coloredImage that is in greyScale
	 */
	public static Picture greyScale(Picture coloredImage) {

		// creates new image with same height and width as coloredImage
		Picture bwImage = new Picture(coloredImage.getWidth(), coloredImage.getHeight());

		int greyScaleValue;

		// loops through all pixels in coloredImage
		for (int x = 0; x < coloredImage.getWidth(); x++) {
			for (int y = 0; y < coloredImage.getHeight(); y++) {

				// computes greyScaleValue at x,y as the average of the R,G,B
				// values at x,y
				greyScaleValue = (coloredImage.getPixelRed(x, y) + coloredImage.getPixelGreen(x, y)
						+ coloredImage.getPixelBlue(x, y)) / 3;

				// bwImage.setPixelRed(x, y, 255 - (int) (Math.random() *
				// greyScaleValue));
				// bwImage.setPixelGreen(x, y, 255 - (int) (Math.random() *
				// greyScaleValue));
				// bwImage.setPixelBlue(x, y, 255 - (int) (Math.random() *
				// greyScaleValue));

				// sets R,G,B as greyScaledValue in bwImage
				bwImage.setPixelRed(x, y, (int) (greyScaleValue));
				bwImage.setPixelGreen(x, y, (int) (greyScaleValue));
				bwImage.setPixelBlue(x, y, (int) (greyScaleValue));

			}
		}

		// return bwImage
		return bwImage;
	}

	/**
	 * Returns scaled copy of given image
	 * 
	 * @param inputImage
	 *            Image to be scaled
	 * @param factor
	 *            Factor for image to be scaled; factor > 0: increase in size;
	 *            factor < 0: decrease in size; factor = 0: no change in size
	 * @return Returns scaled image
	 */
	public static Picture scale(Picture inputImage, double factor) {

		// for increase in size
		if (factor > 0) {

			// creates new image with dimensions scaled up by factor
			Picture outputImage = new Picture((int) (inputImage.getWidth() * factor),
					(int) (inputImage.getHeight() * factor));

			// loops through all pixels in outputImage
			for (int x = 0; x < outputImage.getWidth(); x++) {
				for (int y = 0; y < outputImage.getHeight(); y++) {

					// sets outputImage at x,y at inputImage at (x / factor, y /
					// factor)
					outputImage.setPixelRed(x, y, inputImage.getPixelRed((int) (x / factor), (int) (y / factor)));
					outputImage.setPixelGreen(x, y, inputImage.getPixelGreen((int) (x / factor), (int) (y / factor)));
					outputImage.setPixelBlue(x, y, inputImage.getPixelBlue((int) (x / factor), (int) (y / factor)));

				}
			}

			// returns outputImage
			return outputImage;
		}

		// for decrease in size
		else if (factor < 0) {

			// flip factor
			factor = -1 * factor;

			// creates new image with dimensions scaled down by factor
			Picture outputImage = new Picture((int) (inputImage.getWidth() / factor),
					(int) (inputImage.getHeight() / factor));

			// loops through all pixels in outputImage
			for (int x = 0; x < outputImage.getWidth(); x++) {
				for (int y = 0; y < outputImage.getHeight(); y++) {

					// sets outputImage at x,y at inputImage at (x * factor, y *
					// factor)
					outputImage.setPixelRed(x, y, inputImage.getPixelRed((int) (x * factor), (int) (y * factor)));
					outputImage.setPixelGreen(x, y, inputImage.getPixelGreen((int) (x * factor), (int) (y * factor)));
					outputImage.setPixelBlue(x, y, inputImage.getPixelBlue((int) (x * factor), (int) (y * factor)));

				}
			}

			// return outputImage
			return outputImage;
		}

		// All else: return inputImage
		return inputImage;

	}

	/**
	 * 
	 * Remove a rectangle out of inputImage with corners at xStart, yStart and
	 * xEnd, yEnd
	 * 
	 * xStart, yStart is the top left point of rectangle sub-image xEnd, yEnd is
	 * the bottom right point of rectangle sub-image
	 * 
	 * @param inputImage
	 *            Image from which rectangle sub-image is cropped from
	 * @param xStart
	 *            x coordinate of one corner of sub-image
	 * @param yStart
	 *            y coordinate of one corner of sub-image
	 * @param xEnd
	 *            x coordinate of other corner of sub-image
	 * @param yEnd
	 *            y coordinate of other corner of sub-image
	 * @return Rectangle sub-image cropped out of inputImage
	 */
	public static Picture crop(Picture inputImage, int xStart, int yStart, int xEnd, int yEnd) {

		// creates image with height and width of sub-image
		Picture outputImage = new Picture((int) (xEnd - xStart), (int) (yEnd - yStart));

		// loops through pixels in inputImage that are inside sub-image
		for (int x = xStart; x < xEnd; x++) {
			for (int y = yStart; y < yEnd; y++) {

				// sets outputImage at x - xStart, y - yStart as inputImage at
				// x,y
				outputImage.setPixelRed(x - xStart, y - yStart, inputImage.getPixelRed(x, y));
				outputImage.setPixelGreen(x - xStart, y - yStart, inputImage.getPixelGreen(x, y));
				outputImage.setPixelBlue(x - xStart, y - yStart, inputImage.getPixelBlue(x, y));

			}
		}

		// return outputImage
		return outputImage;

	}

}
