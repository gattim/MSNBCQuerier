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

	private JFrame frame;

	private QueryController queryController;

	private FrontMenuGUI frontMenuGUI;

	private NumUsersOneCat numUsersOneCat;

	private PercentOneCat percentOneCat;

	private MoreUsersTwoCat moreUsersTwoCat;

	private UserViewedCatExplicit userViewedCatExplicit;

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

	public void requestQuery(int type, int cat1) {
		queryController.executeQuery(type, cat1);
	}

	public void requestQuery(int type, int cat1, int cat2) {
		queryController.executeQuery(type, cat1, cat2);
	}

	public void popup(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
		queryController = new QueryController(this);
	}
}