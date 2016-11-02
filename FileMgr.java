package picClass;

//-------------------------------------------------------------------------
//FileMgr Class
/**
 * 23 June 2006
 * 
 * This class has been specially designed for the students of CS-171, to
 * provide an interface to the user, for interaction with the file system.
 * The class provides static methods so it does not need an object to 
 * call the methods.
 * 
 * Version 1.0
 */
//-------------------------------------------------------------------------

/*
 * The swing class is imported to use the JFileChooser object.
 * The io class is imported to write messages to the systyem.
 * The swing class is imported to use the String object.
 * */
import javax.swing.*;

import java.lang.String;

public class FileMgr {
	// -------------------------------------------------------------------------
	/**
	 * Default Constructor shoud not be instantiated directly. The FileMgr class
	 * consists of static methods which do not need an object to be called.
	 */
	// -------------------------------------------------------------------------
	public FileMgr() {
	}

	// -------------------------------------------------------------------------
	// public static String selectExistingFilename()
	/**
	 * Returns the absolute pathname of the selected file as a string. A dialog
	 * box allows the user to traverse through the file system and select a
	 * file.
	 * <p>
	 * <b> Returns: </b> <br>
	 * Returns the absolute pathname for the selected file as a string,
	 * otheriwse the method returns null and sends an error message to the
	 * console.
	 * 
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns null if the user selects cancel or clicks the dialog close box.
	 * <br>
	 * Also returns null if the file does not exist.
	 */
	// -------------------------------------------------------------------------
	public static String selectExistingFilename() {
		int retVal; // Stores the return value.
		JFileChooser fc; // Creating a file chooser object
		String s; // Stores the absolute pathname of the selected file.

		/*
		 * The file chooser object is created relative to the current directory.
		 * A dialog box is opened to allow user to browse through the files on
		 * the system. The show dialog box method returns an integer (which is
		 * stored in retVal) which determines whether the user selected Cancel
		 * or Select. The program continues according to the value stored in
		 * retVal.
		 */

		fc = new JFileChooser(getCurrDirectory());
		retVal = fc.showOpenDialog(null);

		if (retVal == JFileChooser.CANCEL_OPTION) { // if the user selects
													// cancel
			System.err.println("Could not return filename.");
			return null;
		}

		/*
		 * To select file and then determine the absolute pathname of the file
		 * selected above.
		 */

		s = fc.getSelectedFile().getAbsolutePath();

		if (fc.getSelectedFile().isFile()) {

			// if the file exists and is a regular file, return the absolute
			// pathname.

			return s;
		} else {
			// File does not exist, return an error message
			System.err.println("File does not exist.");
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// public static String selectNewFilename()
	/**
	 * Returns the absolute pathname of the file as a string. If the filename
	 * already exists, a dialog box is displayed which asks whether the user
	 * wishes to overwrite an existing file or not. If the user does not wish to
	 * overwrite, then a dialog box opens up to select a new file.
	 * <p>
	 * <b> Returns: </b> <br>
	 * If valid, the absolute pathname for the choosen filename as a string,
	 * otherwise the method returns a null and sends an error mesage to the
	 * console.
	 * 
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns null if user selects cancel or selects the close box. <br>
	 * Also returns null if the filename is already in existance.
	 */
	// -------------------------------------------------------------------------
	public static String selectNewFilename() {
		int retVal; // Stores the return value.
		JFileChooser fc = new JFileChooser(getCurrDirectory());

		/*
		 * The file chooser object is used to open a dialog box to allow users
		 * to browse through the files on the system. It also returns an integer
		 * (which is stored in retVal) which determines whether the user
		 * selected Cancel or Select. The program continues according to the
		 * value stored in retVal.
		 */

		retVal = fc.showSaveDialog(null);
		if (retVal == JFileChooser.CANCEL_OPTION) { // if the user selects
													// cancel
			System.err.println("Could not return filename.");
			return null;
		}

		if (fc.getSelectedFile().exists()) {

			// checks to see if the file exists

			System.err.println("Filename already exists! Returned null");
			return null;
		}

		return fc.getSelectedFile().getAbsolutePath();
	}

	// -------------------------------------------------------------------------
	// public static String selectDirectoryName()
	/**
	 * The method returns a string as the absolute pathname for the directory
	 * that the user wishes to set as default. If the directory name entered
	 * does not exist, the user is asked to input the directory name again.
	 * <p>
	 * <b> Returns: </b> <br>
	 * null if the user selects cancel. <br>
	 * null for wrong input of character.</br>
	 * 
	 * <p>
	 * <b> Errors: </b> <br>
	 * Returns null if the user selects the cancel option. <br>
	 * Returns null if the chosen directory does not exist.
	 */
	// -------------------------------------------------------------------------
	public static String selectDirectoryName() {
		int retVal; // Stores the return value.
		JFileChooser fc = new JFileChooser();

		// allows only directories to be visible
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		/*
		 * The file chooser object is used to open a dialog box so as to allow
		 * users to browse through the files on the system. It also returns an
		 * integer (which is stored in retVal) which determines whether the user
		 * selected Cancel or Select. The program continues according to the
		 * value stored in retVal.
		 */

		retVal = fc.showOpenDialog(null);

		if (retVal == JFileChooser.CANCEL_OPTION) { // if the user selects
													// cancel
			System.err.println(" Could not return directory name");
			return null;
		}

		/*
		 * If the directory typed in by the user does not exist, a null value is
		 * returned.
		 */

		if (!(fc.isTraversable(fc.getSelectedFile()))) {
			System.err.println("The Directory does not exist.");
			return null;
		}

		// Returns the absolute pathname of the selcted file.
		return fc.getSelectedFile().getAbsolutePath();
	}

	// -------------------------------------------------------------------------
	// public static void setCurrDirectory(String s)
	/**
	 * The method allows the user to reset the current directory.
	 * 
	 * <p>
	 * <b> Parameters: </b> <br>
	 * s - The relative (or absolute) pathname of the directory which is to be
	 * set as default
	 * <p>
	 * <b>Errors: </b> <br>
	 * Does not perfom method if s is null.
	 */
	// -------------------------------------------------------------------------
	public static void setCurrDirectory(String s) {
		/*
		 * User can set current working directory within "user.dir" to the
		 * directory passed in as String s.
		 */

		if (s != null)
			System.setProperty("user.dir", s);
	}

	// -------------------------------------------------------------------------
	/**
	 * Returns the current working directory name as a string.
	 * <p>
	 * <b> Returns: </b> <br>
	 * Returns the current working directory as a string.
	 */
	// -------------------------------------------------------------------------
	public static String getCurrDirectory() {
		return System.getProperty("user.dir");
	}
}