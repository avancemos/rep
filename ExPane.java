/**
 * 
 */
package picClass;

//-------------------------------------------------------------------------
// ExPane Class
/**
 * 13 July 2006
 * The class is used for the Explorer class to place a picture on a panel
 * that will be displayable in Explorer.
 * Version 1.0
 * */

//---------------------------------------------------------------------------
import java.awt.*; //imported to access colors
import java.awt.image.BufferedImage; //imported to access BufferedImages
import javax.swing.*; //imported to access JPanel

//---------------------------------------------------------------------------
class ExPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage buff;

	// -------------------------------------------------------------------------
	// public ExPane(BufferedImage b)
	/**
	 * This constructor creates a form of a JPanel that is capable of displaying
	 * images without the use of iconImages and JLabels.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * b - the image passed in to display
	 */
	// -------------------------------------------------------------------------
	public ExPane(BufferedImage b) {
		buff = b; // sets private data to the image
		setSize(b.getWidth(), b.getHeight());
		setVisible(true); // allows visibility
	}

	// -------------------------------------------------------------------------
	// public void paintComponent(Graphics g)
	/**
	 * This method is called by the ExPane class to repaint the panel each time
	 * a change has occured to it. This method should not be invoked by the
	 * user.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * g - the graphics object used to draw the image.
	 */
	// -------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(buff, 0, 0, this);
	}

	// -------------------------------------------------------------------------
	// public void setPic(BufferedImage b)
	/**
	 * This method replaces the existing image with a new image. The steps taken
	 * are the same as in the constructor.
	 * <p>
	 * <b> Parameters: </b> <br>
	 * b - the new image passed into the ExPane object
	 **/
	// -------------------------------------------------------------------------
	public void setPic(BufferedImage b) {
		buff = b;
		setSize(b.getWidth(), b.getHeight());
		setVisible(true);

	}
}
