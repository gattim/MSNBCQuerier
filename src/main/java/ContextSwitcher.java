import javax.swing.*;

/**
* This class controls the changes in the query paradigms. It is the first thing the 
* driver constructs.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class ContextSwitcher {

	/** Main frame of the GUI. */
	private JFrame frame;

	/** 
	* Reference to the query controller in order to be able to communicate with it.
	*/
	private QueryController queryController;

	/** Main menu of the GUI that the program loads into and resets to. */
	private FrontMenuGUI frontMenuGUI;

	/** Query for finding out if a minimum number of users visited a category. */
	private NumUsersOneCat numUsersOneCat;

	/** Query for finding out the percentage of users visited a category. */
	private PercentOneCat percentOneCat;

	/** Query for finding out which of two categories had more visits. */
	private MoreUsersTwoCat moreUsersTwoCat;

	/** Query for finding out how many users visited a certain page an explicit number of times. */
	private UserViewedCatExplicit userViewedCatExplicit;

	/** Query for finding out the percentage of users that visited one category over the other. */
	private PercentUsersTwoCat percentUsersTwoCat;

	/**
	* When this class first gets contructed it creates and shows the front
	* menu GUI frame.
	*/
	public ContextSwitcher() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("MSNBC Querier");

		queryController = new QueryController(this);

		// Create and set up the content pane.
		frontMenuGUI = new FrontMenuGUI(this);
		numUsersOneCat = new NumUsersOneCat(this);
		percentOneCat = new PercentOneCat(this);
		moreUsersTwoCat = new MoreUsersTwoCat(this);
		userViewedCatExplicit = new UserViewedCatExplicit(this);
		percentUsersTwoCat = new PercentUsersTwoCat(this);

		frame.setContentPane(frontMenuGUI.createContentPane());

		// Puts the frame on the screen.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public void switchContext(int i) {
		frame.getContentPane().removeAll();

		switch (i) {
			
			case 0: 
				frame.setContentPane(numUsersOneCat.createContentPane());
				break;
			case 1:
				frame.setContentPane(percentOneCat.createContentPane());
				break;
			case 2:
				frame.getContentPane().add(moreUsersTwoCat.createContentPane());
				break;
			case 3:
				frame.setContentPane(userViewedCatExplicit.createContentPane());
				break;
			case 4:
				frame.setContentPane(percentUsersTwoCat.createContentPane());
				break;
			default:
				frame.getContentPane().add(frontMenuGUI.createContentPane());
				break;
		}

		((JPanel)frame.getContentPane()).revalidate();
      	frame.repaint();
      	frame.pack();
	}

	/**
	* Requests a query be done through the query controller.
	*
	* @param int Specifies the type of query being executed.
	* @param int Category that is being queried.
	*/
	public void requestQuery(int type, int cat1) {
		queryController.executeQuery(type, cat1);
	}

	/**
	* Requests a query be done through the query controller.
	*
	* @param int Specifies the type of query being executed.
	* @param int Category that is being queried.
	* @param int A different category that is being queried against.
	*/
	public void requestQuery(int type, int cat1, int cat2) {
		queryController.executeQuery(type, cat1, cat2);
	}

	/**
	* Creates a popup window showing the results of the query.
	*
	* @param String Message for the popup window to display.
	*/
	public void popup(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
		queryController = new QueryController(this);
	}
}