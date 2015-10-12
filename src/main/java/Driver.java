
import javax.swing.*;

/**
* This is the entry point for the program and where the
* GUI gets constructed. The purpose of this program is to 
* allow the user to query data about visits to the MSNBC 
* website on September 29 1999. 
* <p>
* Resources:
* @see <a href="http://www.codeproject.com/Articles/33536/An-Introduction-to-Java-GUI-Programming">http://wwww.codeproject/com</a>
* @see <a href="http://www.codejava.net/java-se/swing/jcombobox-basic-tutorial-and-examples">http://www.codejava.net</a>
* @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html">https://docs.oracle.com</a>
* @see <a href="http://www.codejava.net/java-se/swing/jlist-custom-renderer-example">http://www.codejava.net</a>
* 
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/

public class Driver {
	
	/**
	* Creates the context switcher that powers the GUI.
	*/
	private static void createContextSwitcher() {
		new ContextSwitcher();
	}

	/**
	* The actual entry point into the program. It schedules a job for the
	* event-dispatching thread: creating and showing this application's GUI.
	*
	* @param args 	String array that holds the supplied 
	*				command-line arguments.
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createContextSwitcher();
			}
		});
	}

}