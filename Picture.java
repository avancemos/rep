package picClass;

//-------------------------------------------------------------------------
// Picture Class
/**
 * 23 June 2006
 * The class allows users to create/modify/save pictures in jpg's, png's, 
 * and bitmap's.
 * Version 1.0
 * */

//-------------------------------------------------------------------------
/*
 * The swing class is imported to allow creation of the Picture class by
 *     extending JFrame.
 * The imageio class is imported to read and store buffered images.
 * The io class is imported to send error messages to the console.
 * The awt class is imported to access color objects, buffered images,
 *     graphic objects, and geometric objects. Also used to import 
 *     ImageObserver to keep track of the loading of an image. The Basic
 *     Stroke class is imported to be able to set the width of the pen.
 * The math class is imported to perform trigonometric operations.
 * */
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.Math;

public class Picture extends JFrame implements ImageObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage buffimg; // the picture
	private Graphics2D g2; // 2D graphics obejct
	private int width;// width of the picture
	private int height;// height of picture
	private int count = 0; // number of times the picture has been modified
	private int x = 0; // X- Coordinate
	private int y = 0; // Y- Coordinate
	private Color color = Color.black; // Color of the 2D objects
	// the angle at which the next operation will be performed.
	private double direction = 0.0;
	private boolean penVisibility; // boolean to store if pen is visible or not.
	private int penWidth = 1; // the width of the pen
	private Stroke stroke; // used to set the properties of graphics object
	private Insets insets; // needed to get the accurate size of the frame.

	// -------------------------------------------------------------------------
	// public Picture(int w, int h)
	/**
	 * Constructor that creates a picture of the given width w and height h
	 * <p>
	 * <b> Parameters: </b> <br>
	 * w - The width of the picture <br>
	 * h - The height of the picture
	 */
	// -------------------------------------------------------------------------
	public Picture(int w, int h) {
		super(); // Invoke the JFrame constructor first.

		// set the height and width to that of the parameters.
		width = w;
		height = h;

		// create a BufferedImage with the standard color system.
		buffimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		rightSize(); /*
						 * Sets the frame to the appropiate size to fully
						 * display the image.
						 */

		/*
		 * Get the graphics of the buffered Image and then set the pen color to
		 * white, draw a rectangle that coveres the entire area of the image.
		 * This is dont so as to ensure that when the file is saved, the
		 * background color is that specified by the user (or a default white)
		 * and not black
		 */
		g2 = (Graphics2D) buffimg.getGraphics();
		setPenColor(Color.white);
		drawRectFill(0, 0, w, h);
		setPenColor(Color.black);
		g2.setColor(color);// to make the pen color a default black.
	}

	// -------------------------------------------------------------------------
	// public Picture (String path)
	/**
	 * Constructor that takes in the pathname as the parameter and creates a
	 * picture. The pathname can be an absolute pathname or pathname relative to
	 * the current working directory.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * path - The absolute pathname of the file.
	 * <p>
	 * <b> Errors: </b> <br>
	 * Sends error message to console if file can not be used to create an
	 * image.
	 */
	// -------------------------------------------------------------------------
	public Picture(String path) {
		super(path); // Invoke the JFrame constructor first

		// This is used to get the information to read a buffered image.
		File pathFile;

		/*
		 * Creates a file from the given string name. If the path name is null
		 * and a file cannot be created, an error is sent to the console. The
		 * program sees if the file can be used to create a buffered image. If
		 * not, an error is sent to the console.
		 */
		try {
			pathFile = new File(path);
		} catch (NullPointerException e) {
			System.err.println(e);
			return;
		}
		try {
			buffimg = ImageIO.read(pathFile);
		} catch (IOException e) {
			System.err.println(e);
			return;
		}

		rightSize(); // Sets the frame to the appropiate size to fully display
						// the image.

		/*
		 * Get the graohics of the buffered Image and set the pen color to a
		 * default black
		 */
		g2 = (Graphics2D) buffimg.getGraphics();
		g2.setColor(color);
	}

	// -------------------------------------------------------------------------
	// private void rightSize()
	/*
	 * The method sets the height and width of the image and checks to see if
	 * the values are valid. If the height and width values are valid, it then
	 * gets the properties of the title bar of the frame in accordance with the
	 * operating system and sets the size based on the insets it receives.
	 * 
	 */
	// -------------------------------------------------------------------------
	private void rightSize() {
		width = buffimg.getWidth();
		height = buffimg.getHeight();

		if (width == -1 || height == -1)
			return;

		addNotify(); /*
						 * Communicates with the operating system to inform it
						 * of the height, width, and insets of the frame.
						 */

		insets = getInsets();
		setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
	}

	// -------------------------------------------------------------------------
	// public boolean imageUpdate(Image img, int infoflags,
	// int x, int y, int width, int height)
	/*
	 * The method checks to see if the loading of the picture is complete. It
	 * returns false if the picture has been completely loaded and returns true
	 * if not. The method is automatically called each time there is a change in
	 * the size of the Buffered Image. <p><b> Parameters: </b> <br> img - The
	 * Image that is being used. <br> infoflags - The status of the image as set
	 * by the image observer. <br> x - The x coordinate. <br> y - The y
	 * coordinate. <br> width - The width of the image. <br> height - The height
	 * of the image. <p><b> Returns: </b> <br> false - if the image has been
	 * fully loaded. <br> true - if the image has not fully loaded.
	 */
	// -------------------------------------------------------------------------
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		if ((infoflags & ImageObserver.ERROR) != 0) {
			System.err.println("Error loading image!");
			System.exit(-1);
		}
		if ((infoflags & ImageObserver.WIDTH) != 0 && (infoflags & ImageObserver.HEIGHT) != 0)
			rightSize();
		if ((infoflags & ImageObserver.SOMEBITS) != 0)
			repaint();
		if ((infoflags & ImageObserver.ALLBITS) != 0) {
			rightSize();
			repaint();
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// public void paint(Graphics g)
	/*
	 * This method overwrites the original paint method of JFrame. This method
	 * should not be instantiated directly.
	 */
	// -------------------------------------------------------------------------
	@Override
	public void paint(Graphics g) {
		super.paint(g); // paints the graphics of the base class
		Graphics2D g2 = (Graphics2D) g;// Creates a Graphics2D object.
		g2.drawImage(buffimg, insets.left, insets.top, this);

	}

	// -------------------------------------------------------------------------
	// public void display()
	/**
	 * Method to display a picture. The method can also be invoked to display
	 * the picture after it has been updated.
	 */
	// -------------------------------------------------------------------------
	public void display() {
		/*
		 * Keeps count of the number of times the image has been drawn/redrawn.
		 * The setVisible method allows the user to see the image. If the image
		 * is being redrawn, the repaint method is called to refresh the image
		 * with the changes made to it.
		 */
		if (count == 0) {
			setVisible(true);
			count++;
		} else {
			/*
			 * If the image is redrawn on the frame then the next time through
			 * the repaint method in the class JFrame is invoked to redraw the
			 * image.
			 */
			setVisible(true);
			repaint();
			count++;
		}
		// setVisible(true);
		// for(int x = 0; x < 1000; x++)
		// {
		// repaint();
		// }
	}

	// -------------------------------------------------------------------------
	// public int getHeight()
	/**
	 * Method to get the height of the picture
	 * <p>
	 * <b> Returns: </b> <br>
	 * Picture height
	 */
	// -------------------------------------------------------------------------
	@Override
	public int getHeight() {
		return buffimg.getHeight();
	}

	// -------------------------------------------------------------------------
	// public int getWidth()
	/**
	 * Method to get the width of the picture
	 * <p>
	 * <b> Returns: </b> <br>
	 * Picture width
	 */
	// -------------------------------------------------------------------------
	@Override
	public int getWidth() {
		return buffimg.getWidth();
	}

	// -------------------------------------------------------------------------
	// public int getPixelRed(int x, int y)
	/**
	 * Returns the intensity of the red component at the coordinate (x,y)
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate
	 * 
	 * <p>
	 * <b> Returns: </b> <br>
	 * Returns the red value at the given x and y
	 */
	// -------------------------------------------------------------------------
	public int getPixelRed(int x, int y) {
		Color colorObj;

		/*
		 * A color object is created to store the retrieved rgb for the
		 * particular coordinate. The red component is returned from the color
		 * stored in the color object.
		 */
		colorObj = new Color(buffimg.getRGB(x, y));
		return colorObj.getRed();
	}

	// -------------------------------------------------------------------------
	// public int getPixelGreen(int x, int y)
	/**
	 * Returns the intensity of the green component at the coordinate (x,y)
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate
	 * <p>
	 * <b> Returns: </b> <br>
	 * Returns the green value at the given x and y
	 */
	// -------------------------------------------------------------------------
	public int getPixelGreen(int x, int y) {
		Color colorObj;

		/*
		 * A color object is created to store the retrieved rgb for the
		 * particular coordinate. The green component is returned from the color
		 * stored in the color object.
		 */
		colorObj = new Color(buffimg.getRGB(x, y));
		return colorObj.getGreen();
	}

	// -------------------------------------------------------------------------
	// public int getPixelBlue(int x, int y)
	/**
	 * Returns the intensity of the blue component at the coordinate (x,y)
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate
	 * 
	 * <p>
	 * <b> Returns: </b> <br>
	 * Returns the blue value at the given x and y
	 */
	// -------------------------------------------------------------------------
	public int getPixelBlue(int x, int y) {
		Color colorObj;

		/*
		 * A color object is created to store the retrieved rgb for the
		 * particular coordinate. The blue component is returned from the color
		 * stored in the color object.
		 */
		colorObj = new Color(buffimg.getRGB(x, y));
		return colorObj.getBlue();
	}

	// -------------------------------------------------------------------------
	// public void setPixelRed(int x, int y, int r)
	/**
	 * Sets the red component of the pixel at coordinate (x,y) to intensity
	 * given by r. Valid values are in the range 0-255
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate <br>
	 * r - The new value
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns an error message to the console if the r parameter is not within
	 * range.
	 */
	// -------------------------------------------------------------------------
	public void setPixelRed(int x, int y, int r) {
		Color colorObj; // Color object for color at the given coordinates
		Color newC; // color created with the new red value

		/*
		 * If the red componenet that needs to be set is within the valid range,
		 * then colorObj stores the original rgb value. newC is the color
		 * created by resetting only the red component. The rgb at the x and y
		 * coordinates is set by getting the rgb of the new color thus created.
		 */
		if (r >= 0 && r <= 255) {
			colorObj = new Color(buffimg.getRGB(x, y));
			newC = new Color(r, colorObj.getGreen(), colorObj.getBlue());
			buffimg.setRGB(x, y, newC.getRGB());
		} else {
			System.err.println("Color value must be from 0-255");
		}
	}

	// -------------------------------------------------------------------------
	// public void setPixelGreen(int x, int y, int g)
	/**
	 * Sets the green component of the pixel at coordinate (x,y) to intensity
	 * given by g. Valid values are in the range 0-255
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate <br>
	 * g - The new value
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns an error message to the console if the g parameter is not within
	 * range.
	 */
	// -------------------------------------------------------------------------
	public void setPixelGreen(int x, int y, int g) {
		Color colorObj; // Color object for color at the given coordinates
		Color newC; // color created with the new red value

		/*
		 * If the green componenet that needs to be set is within the valid
		 * range, then colorObj stores the original rgb value. newC is the color
		 * created by resetting only the green component. The rgb at the x and y
		 * coordinates is set by getting the rgb of the new color thus created.
		 */
		if (g >= 0 && g <= 255) {
			colorObj = new Color(buffimg.getRGB(x, y));
			newC = new Color(colorObj.getRed(), g, colorObj.getBlue());
			buffimg.setRGB(x, y, newC.getRGB());
		} else {
			System.err.println("Color value must be from 0-255");
		}
	}

	// -------------------------------------------------------------------------
	// public void setPixelBlue(int x, int y, int b)
	/**
	 * Sets the blue component of the pixel at coordinate (x,y) to intensity
	 * given by b. Valid values are in the range 0-255
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate <br>
	 * b - The new value
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns an error message to the console if the b parameter is not within
	 * range.
	 */
	// -------------------------------------------------------------------------
	public void setPixelBlue(int x, int y, int b) {
		Color colorObj; // Color object for color at the given coordinates
		Color newC; // color created with the new red value

		/*
		 * If the blue componenet that needs to be set is within the valid
		 * range, then colorObj stores the original rgb value. newC is the color
		 * created by resetting only the blue component. The rgb at the x and y
		 * coordinates is set by getting the rgb of the new color thus created.
		 */
		if (b >= 0 && b <= 255) {
			colorObj = new Color(buffimg.getRGB(x, y));
			newC = new Color(colorObj.getRed(), colorObj.getGreen(), b);
			buffimg.setRGB(x, y, newC.getRGB());
		} else {
			System.err.println("Color value must be from 0-255");
		}
	}

	// -------------------------------------------------------------------------
	// public Color getPixelColor(int x, int y)
	/**
	 * Method to get a color object for the pixel given by (x,y)
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate
	 * <p>
	 * <b> Returns: </b> <br>
	 * Color object for the pixel at (x,y)
	 */
	// -------------------------------------------------------------------------
	public Color getPixelColor(int x, int y) {
		Color colorObj; // color object to get the color at the coordinates.

		/*
		 * The color object thus created is linked to the rgb value at the
		 * particular coordinate and the color is then returned.
		 */
		colorObj = new Color(buffimg.getRGB(x, y));
		return colorObj;
	}

	// -------------------------------------------------------------------------
	// public void setPixelColor(int x, int y, Color c)
	/**
	 * Sets the color of the pixel at (x,y) to c
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate <br>
	 * c - The new color to be set
	 */
	// -------------------------------------------------------------------------
	public void setPixelColor(int x, int y, Color c) {
		int rgbVal; // to store rgb value of the color that is passed in.

		rgbVal = c.getRGB();
		buffimg.setRGB(x, y, rgbVal);
	}

	// -------------------------------------------------------------------------
	// public boolean readFile(String filename)
	/**
	 * Method loads a picture file into the current picture object. It returns a
	 * boolean value of true if the picture has loaded and false otherwise. It
	 * requires the pathname as a string parameter (filename)
	 * <p>
	 * <b> Parameters: </b> <br>
	 * filename - The absolute (or relative) pathname
	 * <p>
	 * <b> Errors: </b> <br>
	 * Sends error message to console if file can not be used to create an
	 * image.
	 */
	// -------------------------------------------------------------------------
	public boolean readFile(String filename) {
		File f;// This is used to get the information to read a buffered image.

		/*
		 * Creates a file from the given string name. If the path name is null
		 * and a file cannot be created, an error is sent to the console. The
		 * program sees if the file can be used to create a buffered image. If
		 * not, an error is sent to the console.
		 */
		try {
			f = new File(filename);
		} catch (NullPointerException e) {
			System.err.println(e);
			return false;
		}
		try {
			buffimg = ImageIO.read(f);
		} catch (IOException ioException) {
			System.err.println("Error!");
			return false;
		}

		rightSize();
		g2 = (Graphics2D) buffimg.getGraphics();
		display();
		return true;
	}

	// -------------------------------------------------------------------------
	// public boolean writeFile(String s)
	/**
	 * Method saves the picture to the user's computer. It supports jpg's,
	 * bitmap's and png's
	 * <p>
	 * <b> Parameters: </b> <br>
	 * s - The absolute pathname
	 * <p>
	 * <b> Returns: </b> <br>
	 * true if the file has been created successfully and false if the file is
	 * not created. <br>
	 * returns false if pathname is null
	 */
	// -------------------------------------------------------------------------
	public boolean writeFile(String s) {

		// If the pathname is null, the method discontinues and false is
		// returned.
		if (s == null)
			return false;

		boolean flag = false;
		File filename = new File(s);

		/*
		 * The varius options available to the user to save a file are printed
		 * out to the console. The user inputs a value and this string received
		 * is then converted to an integer.
		 */
		System.out.println("Enter 1 for saving a jpg.");
		System.out.println("Enter 2 for saving a png.");
		System.out.println("Enter 3 for saving a bitmap.");
		String strVal = JOptionPane.showInputDialog("Please input a value");
		int val = Integer.parseInt(strVal);

		/*
		 * According to the value inputed by the user, the image is then saved
		 * on the disk with the required file extension
		 */
		if (val > 3 || val < 1) {
			System.err.println("Wrong input for saving a file.");
			flag = false;
		} else if (val == 1) {
			flag = errorManagement("jpg", filename);
		} else if (val == 2) {
			flag = errorManagement("png", filename);
		} else if (val == 3) {
			flag = errorManagement("bmp", filename);
		}
		return flag;
	}

	// -------------------------------------------------------------------------
	// private boolean errorManagement(String ext, File f)
	/**
	 * Method is a part of the writeFile method and checks to see if the image
	 * can be written and returns false if it cannot.
	 */
	// -------------------------------------------------------------------------
	private boolean errorManagement(String ext, File f) {
		try {
			ImageIO.write(buffimg, ext, f);
		} catch (IOException ioException) {
			System.err.println(ioException);
			return false;
		} catch (IllegalArgumentException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// public void setPenColor(Color c)
	/**
	 * Sets the color for the drawing of 2D objects
	 * <p>
	 * <b> Parameters: </b> <br>
	 * c - The Color that you wish to set to
	 */
	// -------------------------------------------------------------------------
	public void setPenColor(Color c) {
		color = c;
		g2.setColor(color);
	}

	// -------------------------------------------------------------------------
	// public color getPenColor()
	/**
	 * The method returns a color object for the pen color at current
	 * coordinate.
	 * <p>
	 * <b> Returns: </b> <br>
	 * Color object for the present pen color.
	 */
	// -------------------------------------------------------------------------
	public Color getPenColor() {
		return color;
	}

	// -------------------------------------------------------------------------
	// public void drawLine(int x1, int y1, int x2, int y2)
	/**
	 * Draws a line betweeen (x1,y1) and (x2,y2). After drawing the line, the
	 * current position is (x2,y2).
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x1 - The x co-ordinate of the first point <br>
	 * y1 - The y co-ordinate of the first point <br>
	 * x2 - The x co-ordinate of the second point <br>
	 * y2 - The y co-ordinate of the second point
	 */
	// -------------------------------------------------------------------------
	public void drawLine(int x1, int y1, int x2, int y2) {
		stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		g2.drawLine(x1, y1, x2, y2);

		x = x2; // x is set to x coordinate at the end of the line.
		y = y2; // y is set to y coordinate at the end of the line.
	}

	// -------------------------------------------------------------------------
	// public void drawLine(int x, int y)
	/**
	 * Draws a line from the present coordinate to the other coordinate
	 * specified (x,y).After drawing the line, the current position is (x,y)
	 * which are the coordinates passed in.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x co-ordinate of the second point <br>
	 * y - The y co-ordinate of the second point
	 */
	// -------------------------------------------------------------------------
	public void drawLine(int x, int y) {
		Stroke stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		g2.drawLine(this.x, this.y, x, y);

		this.x = x; // x is set to x coordinate at the end of the line.
		this.y = y; // y is set to y coordinate at the end of the line.
	}

	// -------------------------------------------------------------------------
	// public void drawCircle(int x, int y, int radius)
	/**
	 * Draws an ouline of a circle at the x and y coordinates and the radius
	 * given by user. The current x and y positions is set to the centre of the
	 * circle.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate of the centre of the circle. <br>
	 * y - The y coordinate of the centre of the circle. <br>
	 * radius - The radius of the circle to be drawn
	 */
	// -------------------------------------------------------------------------
	public void drawCircle(int x, int y, int radius) {
		this.x = x - radius;// x is set to x coordinate of the center of the
							// circle.
		this.y = y - radius;// y is set to y coordinate of the center of the
							// circle.

		stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		g2.setColor(color);

		/*
		 * creates an oval with the height and the width as the diameter of the
		 * circle that thee user intends to create.
		 */
		g2.drawOval(this.x, this.y, radius * 2, radius * 2);
	}

	// -------------------------------------------------------------------------
	// public void drawCircleFill(int x, int y, int radius)
	/**
	 * Draws a filled circle of the radius given at the coordinate (x,y). The
	 * circle is drawn with the current color. The current positon is set to the
	 * centre of the circle.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate of the centre of the circle. <br>
	 * y - The y coordinate of the centre of the circle. <br>
	 * radius - The radius of the circle to be drawn.
	 */
	// -------------------------------------------------------------------------
	public void drawCircleFill(int x, int y, int radius) {
		this.x = x - radius;// x is set to x coordinate of the center of the
							// circle.
		this.y = y - radius;// y is set to y coordinate of the center of the
							// circle.

		/*
		 * draws an filled ellipse with the height and width as the diameter of
		 * the circle the user intends to create.
		 */
		g2.fill(new Ellipse2D.Double(this.x, this.y, radius * 2, radius * 2));
	}

	// -------------------------------------------------------------------------
	// public void drawEllipse(int x,int y, int minor, int major)
	/**
	 * Method draws the outline of an Ellipse given the x and y coordinates with
	 * the lenght of the major and minor axis. The current position is set to
	 * the centre of the ellipse
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The y coordinate <br>
	 * minor - The lenght of the minor axis <br>
	 * major - The lenght of the major axis.
	 */
	// -------------------------------------------------------------------------
	public void drawEllipse(int x, int y, int minor, int major) {
		this.x = x; // x is set to x coordinate at the centre of Ellipse.
		this.y = y; // y is set to y coordinate at the centre of Ellipse.

		stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		g2.draw(new Ellipse2D.Double(this.x, this.y, minor, major));
	}

	// -------------------------------------------------------------------------
	// public void drawEllipseFill(int x,int y, int minor, int major)
	/**
	 * Method draws a filled Ellipse given the x and y coordinates with the
	 * lenght of the major and minor axis. The current position is set to the
	 * centre of the ellipse
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate <br>
	 * y - The Y coordinate <br>
	 * minor - The lenght of the minor axis <br>
	 * major - The lenght of the major axis.
	 */
	// -------------------------------------------------------------------------
	public void drawEllipseFill(int x, int y, int minor, int major) {
		this.x = x; // x is set to x coordinate at the centre of Ellipse.
		this.y = y; // y is set to y coordinate at the centre of Ellipse.
		g2.fill(new Ellipse2D.Double(this.x, this.y, minor, major));
	}

	// -------------------------------------------------------------------------
	// public void drawRect(int x, int y, int w, int h)
	/**
	 * Draws the outline of the rectangle at (x,y) with the height h and width
	 * w. The current position is set to the top left corner of the rectangle.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate of the top left corner of the rectangle <br>
	 * y - The y coordinate of the top left corner of the rectangle <br>
	 * w - The width of the rectangle <br>
	 * h - The height of the rectangle
	 */
	// -------------------------------------------------------------------------
	public void drawRect(int x, int y, int w, int h) {
		stroke = new BasicStroke(penWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);
		g2.drawRect(x, y, w, h);

		/*
		 * The x and the y coordinate are set to the values of the x and y of
		 * the top left corner of the rectangle
		 */
		this.x = x;
		this.y = y;
	}

	// -------------------------------------------------------------------------
	// public void drawRectFill( int x , int y , int w, int h)
	/**
	 * Draws the rectangle at (x,y) with the height h and width w. Also fills up
	 * the rectangle with the current color. The current position is set to the
	 * top left corner of the rectangle.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x coordinate of the top left corner of the rectangle <br>
	 * y - The y coordinate of the top left corner of the rectangle <br>
	 * w - The width of the rectangle <br>
	 * h - The height of the rectangle
	 */
	// -------------------------------------------------------------------------
	public void drawRectFill(int x, int y, int w, int h) {
		g2.fill(new Rectangle(x, y, w, h));
		/*
		 * The x and the y coordinate are set to the values of the x and y of
		 * the top left corner of the rectangle
		 */
		this.x = x;
		this.y = y;
	}

	// -------------------------------------------------------------------------
	// public void setPenUp()
	/**
	 * Sets the pen up.
	 */
	// -------------------------------------------------------------------------
	public void setPenUp() {
		// sets alpha transparency to O so that color is not seen
		Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
		color = c;
		g2.setColor(c);
		penVisibility = false;
	}

	// -------------------------------------------------------------------------
	// public void setPenDown()
	/**
	 * Sets the pen down
	 */
	// -------------------------------------------------------------------------
	public void setPenDown() {
		// sets alpha transparency to 255 so that the color is seen
		Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
		color = c;
		g2.setColor(color);
		penVisibility = true;
	}

	// -------------------------------------------------------------------------
	// public boolean isPenUp()
	/**
	 * Checks to see if the Pen is up. Returns true if the pen is up and false
	 * if it is not.
	 * <p>
	 * <b> Returns: </b> <br>
	 * boolean to show pen status
	 */
	// -------------------------------------------------------------------------
	public boolean isPenUp() {
		if (penVisibility == false)
			return true;
		else
			return false;
	}

	// -------------------------------------------------------------------------
	// public void setX (int x)
	/**
	 * Changes the x coordinate to that specified by the user
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x - The x co-ordinate of the new point.
	 */
	// -------------------------------------------------------------------------
	public void setX(int x) {
		this.x = x;
	}

	// -------------------------------------------------------------------------
	// public void setY (int y)
	/**
	 * Changes the y coordinate to that specified by the user
	 * <p>
	 * <b> Parameters: </b> <br>
	 * y - The y co-ordinate of the new point.
	 */
	// -------------------------------------------------------------------------
	public void setY(int y) {
		this.y = y;
	}

	// -------------------------------------------------------------------------
	// public void setPosition(int x, int y)
	/**
	 * Method sets the pen to the x,y coordinate of the user's choice.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * x = the new x coordinate <br>
	 * y = the new y coordinate
	 */
	// -------------------------------------------------------------------------
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// -------------------------------------------------------------------------
	// public void setDirection(double d)
	/**
	 * Method sets the current direction to that of the user's choice. Negative
	 * degrees are mesured clockwise. Positive degrees are measure
	 * counterclockwise
	 * <p>
	 * <b> Parameters: </b> <br>
	 * d The degree of the direction
	 */
	// -------------------------------------------------------------------------
	public void setDirection(double d) {
		direction = d;
	}

	// -------------------------------------------------------------------------
	// public double getDirection()
	/**
	 * Method returns the current direction in degrees.
	 * <p>
	 * <b> Returns: </b> <br>
	 * The degree of current direction
	 */
	// -------------------------------------------------------------------------
	public double getDirection() {
		return direction;
	}

	// -------------------------------------------------------------------------
	// public void drawForward(int dist)
	/**
	 * Method shifts the current positon by moving forward with the value that
	 * the user wishes in accordance with the current pen width.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * dist - The number of pixels by which to go forward
	 */
	// -------------------------------------------------------------------------
	public void drawForward(int dist) {
		double deltaX, deltaY;
		double radians;

		/*
		 * The line needs to be drawn in the same direction as the amount of
		 * degree. Trignometry is used to find the coordinates of the point to
		 * which the line needs to be drawn using the distance that has been
		 * received.The cosine of the angle gives the x coordinate and the sine
		 * of the angle gives the y coordinate of the new point.
		 */
		radians = Math.toRadians(90 + direction);
		deltaX = (penWidth) * Math.cos(radians);
		deltaY = (penWidth) * Math.sin(radians);

		/*
		 * since deltaX and deltaY represent the value of the new point in a
		 * coordinate system where the starting point(x,y) is considered as
		 * (0,0); they need to be converted to the values in the coordinate
		 * system of the picture. The new coordinates also have to be
		 * represented as integers so as to draw the line.
		 */
		int x1 = x + (int) Math.rint(deltaX);
		int y1 = y - (int) Math.rint(deltaY);

		radians = Math.toRadians(direction);
		deltaX = (dist) * Math.cos(radians);
		deltaY = (dist) * Math.sin(radians);
		int x2 = x1 + (int) Math.rint(deltaX);
		int y2 = y1 - (int) Math.rint(deltaY);

		radians = Math.toRadians(270 + direction);
		deltaX = penWidth * Math.cos(radians);
		deltaY = penWidth * Math.sin(radians);
		int x3 = x2 + (int) Math.rint(deltaX);
		int y3 = y2 - (int) Math.rint(deltaY);

		radians = Math.toRadians(180 + direction);
		deltaX = (dist) * Math.cos(radians);
		deltaY = (dist) * Math.sin(radians);
		int x4 = x3 + (int) Math.rint(deltaX);
		int y4 = y3 - (int) Math.rint(deltaY);

		/*
		 * Creating two separate arrays storing the values of the x and y
		 * coordinates of the rectangle that needs to be drawn.
		 */
		int[] xarray = { x1, x2, x3, x4 };
		int[] yarray = { y1, y2, y3, y4 };
		int npoints = 4;
		g2.fillPolygon(xarray, yarray, npoints);

		/*
		 * Set current position to the mid-point of the other height of the
		 * rectangle created.
		 */
		x = (x3 + x2) / 2;
		y = (y3 + y2) / 2;
	}

	// -------------------------------------------------------------------------
	// public int getX()
	/**
	 * Method returns the current X Position
	 * <p>
	 * <b> Returns: </b> <br>
	 * The X coordinate of the current position
	 */
	// -------------------------------------------------------------------------
	@Override
	public int getX() {
		return x;
	}

	// -------------------------------------------------------------------------
	// public int getY()
	/**
	 * Method returns the current Y Position
	 * <p>
	 * <b> Returns: </b> <br>
	 * The Y coordinate of the current position
	 */
	// -------------------------------------------------------------------------
	@Override
	public int getY() {
		return y;
	}

	// -------------------------------------------------------------------------
	// public void setPenWidth(int pWidth)
	/**
	 * Method sets the width of the pen
	 * <p>
	 * <b> Parameters: </b> <br>
	 * pWidth - The new width of the pen
	 */
	// -------------------------------------------------------------------------
	public void setPenWidth(int pWidth) {
		penWidth = pWidth;
	}

	// -------------------------------------------------------------------------
	// public int getPenWidth(int pWidth)
	/**
	 * Method returns the width of the pen
	 * <p>
	 * <b> Returns: </b> <br>
	 * The width of the pen
	 */
	// -------------------------------------------------------------------------
	public int getPenWidth() {
		return penWidth;
	}
}