package picClass;

//package picClass;

//-------------------------------------------------------------------------
// Explorer Class
/**
 * 13 July 2006
 * The class allows users to display a picture and access the pixel data 
 * as well as zoom in and zoom out a picture
 * Version 1.0
 * */

//-------------------------------------------------------------------------
import javax.swing.*; //imported to access swing components
import javax.imageio.*; //imported to read in images
import java.awt.*; //imported to access colors and graphics
import java.awt.image.*; //imported to access the bufferedImage
import java.io.*; //imported to create files

//imported to access event listeners for the mouse and keyboard
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//---------------------------------------------------------------------------
class Explorer {
	private JFrame frame; // to hold the panel
	private JScrollPane backgroundPane; // to allow scrolling of large images

	private BufferedImage buffImg;// The buffered image that wiil be displayed
	private BufferedImage b; // The buffered image after a zoom has occured

	// Buttons that are associated with the zooming in and zooming out commands
	private JButton zoomIn;
	private JButton zoomOut;
	private ExPane testPane; // Panel that has the picture from ExPane class

	/*
	 * Panel that has buttons, labels and texts to show pixel data from this
	 * buffered image
	 */
	private JPanel topPanel;
	private JPanel redPanel; // panel to show the red component
	private JPanel greenPanel; // panel to show the green component
	private JPanel bluePanel; // panel to show the blue component

	/*
	 * Labels to show the red, greeen, and blue components on the corresponding
	 * panels.
	 */
	private JLabel redText;
	private JLabel greenText;
	private JLabel blueText;
	private JLabel xLabel; // Labels to show the selected X coordinate
	private JLabel yLabel; // Labels to show the selected Y coordinate

	// Textfields to show the x & y coordinates on the xLabel and yLabel
	private JTextField xText;
	private JTextField yText;

	private MouseListener event; // MouseListener for ExPane and JButtons
	private DocumentListener d; // DocumentListener for JTextFields

	private int x = 0; // Current x-coordinate
	private int y = 0; // Current y-coordinate
	private int count = 0; // An integer that keeps count of the zooming

	// -------------------------------------------------------------------------
	// public Explorer(String pathname)
	/**
	 * The constructor for creating the frame to view pixel information on the
	 * given picture.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * pathname - This is pathname for the picture the user wishes to view.
	 */
	// -------------------------------------------------------------------------
	public Explorer(String pathname) {
		frame = new JFrame("Explorer"); // frame to display the explorer
		File filename = new File(pathname);

		/*
		 * Reads in buffered image with the given filename. If the picture does
		 * not exists, the program stops.
		 */
		try {
			buffImg = ImageIO.read(filename);
		} catch (IOException ioException) {
			System.err.print(ioException);
		}

		// Initialize the rest of the private data.
		topPanel = new JPanel();
		testPane = new ExPane(buffImg);
		xLabel = new JLabel("X:");
		yLabel = new JLabel("Y:");
		redPanel = new JPanel();
		greenPanel = new JPanel();
		bluePanel = new JPanel();
		redText = new JLabel("--");
		greenText = new JLabel("--");
		blueText = new JLabel("--");
		xText = new JTextField("0", 3);
		yText = new JTextField("0", 3);
		zoomIn = new JButton("Zoom In");
		zoomOut = new JButton("Zoom Out");
		backgroundPane = new JScrollPane(testPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		event = new Listen();
		d = new Feel();

		frame.setSize(700, 700); // sets the size of the frame

		/*
		 * Sets the size for the ExPane and the JScrollPane so that the image
		 * will show. The JScrollPane must be bigger than the ExPane at all
		 * times.
		 */
		backgroundPane.setPreferredSize(new Dimension((buffImg.getWidth()) + 1, (buffImg.getHeight()) + 1));
		testPane.setPreferredSize(new Dimension(buffImg.getWidth(), buffImg.getHeight()));

		topPanel.setBackground(Color.white); // Sets top panel's color.
		redPanel.setBackground(Color.red); // sets color for red component
		greenPanel.setBackground(Color.green); // sets color for green component
		bluePanel.setBackground(Color.cyan); // sets color for blue component

		redPanel.add(redText); // sets textfield in the red panel
		greenPanel.add(greenText); // sets textfield in the green panel
		bluePanel.add(blueText); // sets textfield in the blue panel

		testPane.addMouseListener(event); // connects MouseListener to the pane
		zoomIn.addMouseListener(event); // connects MouseListener to zoomIn
										// button
		zoomOut.addMouseListener(event); // connects MouseListener to zoomOut
											// button

		// connects DocumentListener to the x and y textfields
		xText.getDocument().addDocumentListener(d);
		yText.getDocument().addDocumentListener(d);

		topPanel.add(zoomIn); // puts the zoomIn button into the top panel
		topPanel.add(zoomOut); // puts the zoomOut button into top panel
		topPanel.add(xLabel); // puts the xLabel into top panel
		topPanel.add(xText); // puts xText into top panel
		topPanel.add(yLabel); // puts yLabel into top panel
		topPanel.add(yText); // puts the yText into top panel
		topPanel.add(redPanel); // puts the red component into top panel
		topPanel.add(greenPanel); // puts the green component into top panel
		topPanel.add(bluePanel); // puts the blue component into top panel

		// set the view port of the scroll pane to view the image
		backgroundPane.setViewportView(testPane);

		/*
		 * Adds JScrollPane in the center and top panel at the top of the frame
		 * using BorderLayout.
		 */

		frame.add(backgroundPane, BorderLayout.CENTER);
		frame.add(topPanel, BorderLayout.NORTH);
	}

	// -------------------------------------------------------------------------
	// public void display()
	/**
	 * Method to display the picture.
	 */
	// -------------------------------------------------------------------------
	public void display() {
		frame.setVisible(true);
	}

	// -------------------------------------------------------------------------
	// private void zoomInImage()
	/**
	 * This method creates an image 2x the size of the given image. Users cannot
	 * zoom past 4x the original image's size.
	 */
	// -------------------------------------------------------------------------
	private void zoomInImage() {
		int userfactor; // the integer to increase by
		int newHeight; // the new image's height
		int newWidth; // the new image's width

		userfactor = 2;
		newHeight = (int) buffImg.getHeight() * userfactor;
		newWidth = (int) buffImg.getWidth() * userfactor;
		b = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		/*
		 * This nested for loop goes through each of the pixel elements in the
		 * existing picture, divides by the userfactor and explodes the picture
		 * to 2x the size of the existing image.
		 */
		for (int destX = 0; destX < newWidth; destX++) {
			for (int destY = 0; destY < newHeight; destY++) {
				int sourceX = destX / userfactor;
				int sourceY = destY / userfactor;

				int sourceColor = buffImg.getRGB(sourceX, sourceY);
				b.setRGB(destX, destY, sourceColor);
			}
		}

		/*
		 * Sets the size for the ExPane and the JScrollPane so that the image
		 * will show. The JScrollPane must be bigger than the ExPane.
		 */
		backgroundPane.setPreferredSize(new Dimension((b.getWidth()) + 1, (b.getHeight()) + 1));
		testPane.setPreferredSize(new Dimension(b.getWidth(), b.getHeight()));
		buffImg = b; // sets the exisiting image to the new image
		testPane.setPic(b); // sets the display to the new image
		count++;
	}

	// -------------------------------------------------------------------------
	// private void zoomOutImage()
	/**
	 * This method creates an image 1/2x the size of the proceeding image. Users
	 * cannot zoom out from the original image's size.
	 */
	// -------------------------------------------------------------------------
	private void zoomOutImage() {
		int userfactor; // the integer to decrease by
		int newHeight; // the new image's height
		int newWidth; // the new image's width

		userfactor = 2;
		newHeight = (int) buffImg.getHeight() / userfactor;
		newWidth = (int) buffImg.getWidth() / userfactor;
		b = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		/*
		 * This nested for loop goes through each of the pixel elements in the
		 * existing picture, multiplies by the userfactor and shinks the picture
		 * to half the size of the existing image.
		 */
		for (int destX = 0; destX < newWidth; destX++) {
			for (int destY = 0; destY < newHeight; destY++) {
				int sourceX = destX * userfactor;
				int sourceY = destY * userfactor;

				int sourceColor = buffImg.getRGB(sourceX, sourceY);
				b.setRGB(destX, destY, sourceColor);
			}
		}

		/*
		 * Sets the size for the ExPane and the JScrollPane so that the image
		 * will show. The JScrollPane must be bigger than the ExPane.
		 */
		backgroundPane.setPreferredSize(new Dimension((b.getWidth()) + 1, (b.getHeight()) + 1));
		testPane.setPreferredSize(new Dimension(b.getWidth(), b.getHeight()));
		buffImg = b; // sets the exisiting image to the new image
		testPane.setPic(b); // sets the display to the new image
		count--;
	}

	// -------------------------------------------------------------------------
	// class Listen implements MouseListener
	/**
	 * This underclass calls for the methods in MouseListener and reconfigures
	 * the methods to listen for events in the Pane and JButton objects.
	 */
	// -------------------------------------------------------------------------
	class Listen implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == zoomIn) { // if the zoom in button is clicked
				if (count == 2) // more than 2 will cause memory to run out
					System.err.println("Sorry. Cannot zoom in.");
				else
					zoomInImage();
			}
			if (e.getSource() == zoomOut) { // if the zoom out button is clicked
				if (count == 0)
					System.err.println("Sorry. Cannot zoom out.");
				else
					zoomOutImage();
			} else {
				/*
				 * If the user clicks on the pane, the coordinates of the
				 * picture are divided by 2 to the n power to give the
				 * appropiate scaled coordinates. This is done to match the
				 * coordinates of the original picture.
				 */
				xText.setText(Integer.toString(e.getX() / (int) (Math.pow(2, count))));
				yText.setText(Integer.toString(e.getY() / (int) (Math.pow(2, count))));
			}
		}

		// Blank methods
		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	// -------------------------------------------------------------------------
	// class Feel implements DocumentListener
	/**
	 * This underclass calls for the methods in the DocumentListener class and
	 * reconfigures the methods to listen for change in the textfields.
	 */
	// -------------------------------------------------------------------------
	class Feel implements DocumentListener {
		public void insertUpdate(DocumentEvent d) {
			extFeel();
		}

		public void removeUpdate(DocumentEvent d) {
			extFeel();
		}

		// only changes if the style of the text has changed
		public void changedUpdate(DocumentEvent d) {
		}

		// -------------------------------------------------------------------------
		// private void extFeel(DocumentEvent d)
		/**
		 * This private method retrieves the x & y from the textfields and
		 * displays the color values for the cooresponding coordinates. Since
		 * the MouseListener can change the textfields by removing text from the
		 * field and placing the new text inside the field, x & y are set to 0
		 * to avoid error messages when the fields are erased however it is not
		 * noticable in the explorer.
		 * <p>
		 * <b> Parameters: </b> <br>
		 * d - The DocumentEvent created at the time of the change in the
		 * textfield.
		 * <p>
		 * <b> Errors: </b> <br>
		 * Sends error message if the textfields are not integers.
		 */
		// -------------------------------------------------------------------------
		private void extFeel() {
			if (xText.getText().equals("")) // occurs when mouse clicks on
											// testpanel
				x = 0; // puts in a value to avoid error
			else
				x = Integer.parseInt(xText.getText());// gets the value from
														// textfield
			if (yText.getText().equals("")) // occurs when mouse clicks on panel
				y = 0; // puts in a value to avvoid error
			else
				y = Integer.parseInt(yText.getText());// gets the value from
														// textfield

			/*
			 * Gets the color of the specified pixel coordinate and sets the
			 * red, green and blue values into the appropiate labels.
			 */
			Color c = new Color(buffImg.getRGB(x * (int) (Math.pow(2, count)), y * (int) (Math.pow(2, count))));
			redText.setText(Integer.toString(c.getRed()));
			greenText.setText(Integer.toString(c.getGreen()));
			blueText.setText(Integer.toString(c.getBlue()));
		}
	}
}