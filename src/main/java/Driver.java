import javax.swing.*;
import javax.swing.text.*;
import javax.swing.SpringLayout;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class Driver implements ActionListener {

	// Definition of global values and items that are part of the GUI.

	/** 
	* Associated version number which is used during deserialization
	* to verify that the sender and receiver of the object have loaded 
	* classes that are compatible.
	*/
	private static final long serialVersionUID = 1L;
	
	/** 
	* Main panel's JComboBox values which contains the type of 
	* queries the user can perform.
	*/
	private String[] queryTypes = new String[] {
		"Are there more than ____ users who looked at X",
		"What percent of users looked at X",
		"Are there more users who looked at X than Y",
		"How many users viewed X ____ number of times",
		"What percent of users looked at X more than Y"
	};

	/** JComboBox values for the web page categories in the dataset. */
	private String[] categories = new String[] {
		"Front Page", "News", "Tech", "Local", "Opinion",
		"On-Air", "Misc", "Weather", "MSN-News", "Health",
		"Living", "Business", "MSN-Sports", "Sports", "Summary",
		"BBS", "Travel"
	};

	/** Panel that holds just the combobox for selected the query type. */
	private JPanel queryTypePane;

	/** 
	* Panel that holds query info for if a category has more than 
	* a certain number of user visits. 
	*/
	private JPanel numUsersOneCatPane;

	/** Panel that holds query info for the percent of visits to a category. */
	private JPanel percentOneCatPane;

	/** 
	* Panel that holds query info for if a category has more user views 
	* than another category. 
	*/
	private JPanel moreUsersTwoCatPane;

	/**
	* Panel that hold query info for the number of users that viewed a
	* category an explicit number of times.
	*/
	private JPanel userViewedCatExplicitPane;

	/**
	* Panel that holds query info for the percent of users that viewed
	* one category over a different category.
	*/
	private JPanel percentUsersTwoCatPane;

	/** Panel that holds the buttons for querying and reseting. */
	private JPanel buttonPane;

	/** 
	* Label for the numUsersOneCatPane that is 
	* "Are there more than". 
	*/
	private JLabel numUsersOneCat1Lbl;

	/** 
	* Label for the numUsersOneCatPane that is 
	* "users who looked at". 
	*/
	private JLabel numUsersOneCat2Lbl;

	/** 
	* Label for the percentOneCatPane and percentUsersTwoCatPane that is 
	* "What percent of users looked at". 
	*/
	private JLabel percentCatLbl;

	/** 
	* Label for the moreUsersTwoCatPane that is 
	* "Are there more users that looked at". 
	*/
	private JLabel moreUsersTwoCat1Lbl;

	/** Label for the moreUsersTwoCatPane that is "than". */
	private JLabel moreUsersTwoCat2Lbl;

	/** 
	* Label for the userViewedCatExplicitPane that is 
	* "How many users viewed". 
	*/
	private JLabel userViewedCatExplicit1Lbl;

	/** 
	* Label for the userViewedCatExplicitPane that is 
	* "number of times?". 
	*/
	private JLabel userViewedCatExplicit2Lbl;

	/** Label for the percentUsersTwoCatPane that is "more than". */
	private JLabel percentUsersTwoCatLbl;

	/** Label for the "?" at the end of every query. */
	private JLabel questionMarkLbl;

	/** Combobox that holds the different possible queries. */
	private JComboBox<String> queryCb;

	/** Combobox that holds the categories. */
	private JComboBox<String> categories1Cb;

	/** 
	* Second combobox that holds the categories for cases where two 
	* categories are being queried. 
	*/
	private JComboBox<String> categories2Cb;

	/** Spinner to ensure users input valid data. */
	private JSpinner numViewsSp;

	/** Button to initiate the query. */
	private JButton queryBtn;

	/** Button to reset the query. */
	private JButton resetBtn;

	/**
	* Creates the content panel for the GUI. It first creates the bottom
	* level panel and then builds the query panel and the button panel
	* on top of it.
	*
	* @return The content panel for the GUI.
	*/
	public JPanel createContentPane() {		
		// Bottom panel that everything gets placed on.
		JPanel totalGUIPane = new JPanel();
		totalGUIPane.setLayout(new BoxLayout(totalGUIPane, BoxLayout.Y_AXIS));

		JPanel queryPane = createQueryPane();
		JPanel btnPane = createButtonPane();
		totalGUIPane.add(queryPane);
		totalGUIPane.add(btnPane);

		return totalGUIPane;
	}

	/**
	* Creates the query panel and returns it to the content pane
	* to be added.
	*
	* @return The constructed query panel.
	*/
	public JPanel createQueryPane() {
		queryCb = new JComboBox<String>(queryTypes);
		
		queryTypePane = new JPanel();
		queryTypePane.setLayout(null);
		queryTypePane.add(queryCb);

		return queryTypePane;
	}

	/**
	* Creates the button panel with the query and reset buttons in a spring layout.
	*
	* @return The constructed button panel.
	*/
	public JPanel createButtonPane() {
		// Set up the panel.
		buttonPane = new JPanel();
		SpringLayout layout = new SpringLayout();
		buttonPane.setLayout(layout);

		// Create and add the components.
		queryBtn = new JButton("Query");
		resetBtn = new JButton("Reset");
		buttonPane.add(queryBtn);
		buttonPane.add(resetBtn);

		// Adjust the constraints to make them readable.
		SpringLayout.Constraints queryBtnCons = layout.getConstraints(queryBtn);
		SpringLayout.Constraints resetBtnCons = layout.getConstraints(resetBtn);
		queryBtnCons.setX(Spring.constant(5));
		queryBtnCons.setY(Spring.constant(5));
		resetBtnCons.setX(Spring.sum(Spring.constant(5), 
			queryBtnCons.getConstraint(SpringLayout.EAST)));
		resetBtnCons.setY(Spring.constant(5));

		// Adjust constraints for the button panel.
		setButtonPaneSize();

		return buttonPane;
	}

	public void setButtonPaneSize() {
		return;
	}

	/**
	* Catches any events with an ActionListener attached.
	* Using an if statement, we can determine which button was
	* pressed and change the appropriate values in our GUI.
	*/
	public void actionPerformed(ActionEvent e) {

	}

	/**
	* Creates the GUI. It:
	* - sets the title to "MSNBC Querier"; 
	* - changes the size of the window;
	* - specifies that the close button exits the application;
	* - inherit main frame and adds our main JPanel;
	* - Makes the frame visible
	*/
	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("MSNBC Querier");

		// Create and set up the content pane.
		Driver driver = new Driver();
		frame.setContentPane(driver.createContentPane());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(280,190);
		frame.setVisible(true);
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
				createAndShowGUI();
			}
		});
	}

}