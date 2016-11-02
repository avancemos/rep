package picClass;

public class AvaAddImg {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String pathToBaseImg = args[0];
		String pathToLogo = args[1];
		int x = Integer.parseInt(args[2]);
		int y = Integer.parseInt(args[3]);

		Picture baseImg = new Picture(pathToBaseImg);

		Picture addedImg = new Picture(pathToLogo);

		EEGreyScale.copyPic(baseImg, addedImg, x, y);

	}

}
