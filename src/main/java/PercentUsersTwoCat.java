import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
* This is the content panel for the query of the percentage one category was visited
* over another.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class PercentUsersTwoCat {

	// Definition of global values and items that are part of the GUI.

	/** 
	* Associated version number which is used during deserialization
	* to verify that the sender and receiver of the object have loaded 
	* classes that are compatible.
	*/
	private static final long serialVersionUID = 1L;

	/** JComboBox values for the web page categories in the dataset. */
	private String[] categories = new String[] {
		"Front Page", "News", "Tech", "Local", "Opinion",
		"On-Air", "Misc", "Weather", "MSN-News", "Health",
		"Living", "Business", "MSN-Sports", "Sports", "Summary",
		"BBS", "Travel"
	};

	/** Panel that holds just the combobox for selected the query type. */
	private JPanel queryPane;

	/** Panel that holds just the buttons for user executions. */
	private JPanel buttonPane;

	/** Combobox that holds the different categories. */
	private JComboBox<String> catCb1;

	/** Combobox that holds the different categories. */
	private JComboBox<String> catCb2;

	/** Reference to parent context switcher. */
	private ContextSwitcher parentCS;

	/** Button to initiate the query. */
	private JButton queryBtn;

	/** Button to reset the query. */
	private JButton resetBtn;

	/**
	* Constructor that strores a reference to the parent context switcher.
	*
	* @param cs Parent context switcher.
	*/
	public PercentUsersTwoCat(ContextSwitcher cs) {
		parentCS = cs;
	}

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
		totalGUIPane.setLayout(new BoxLayout(totalGUIPane, BoxLayout.PAGE_AXIS));

		// Define the dimensions of the filler space between components.
		Dimension minSize = new Dimension(10,10);
		Dimension prefSize = new Dimension(10,10);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 10);
		
		// Add components to the content panel.
		totalGUIPane.add(new Box.Filler(minSize, prefSize, maxSize));
        totalGUIPane.add(createQueryPane());
        totalGUIPane.add(new Box.Filler(minSize, prefSize, maxSize));
        totalGUIPane.add(createButtonPane());
        totalGUIPane.add(new Box.Filler(minSize, prefSize, maxSize));

        totalGUIPane.setOpaque(true);
		return totalGUIPane;
	}

	/**
	* Creates the query panel and returns it to the content pane
	* to be added.
	*
	* @return The constructed query panel.
	*/
	public JPanel createQueryPane() {
		// Set up the panel.
		queryPane = new JPanel();

		// Creates the input means for the user that gets the categories compared.
		catCb1 = new JComboBox<String>(categories);
		catCb2 = new JComboBox<String>(categories);

        queryPane.setLayout(new BoxLayout(queryPane, BoxLayout.X_AXIS));

        // Adds components to the query panel.
        queryPane.add(Box.createRigidArea(new Dimension(10,0)));
        queryPane.add(new JLabel("What percent of users looked at "));
        queryPane.add(Box.createRigidArea(new Dimension(10,0)));
		queryPane.add(catCb1);
		queryPane.add(Box.createRigidArea(new Dimension(10,0)));
        queryPane.add(new JLabel(" more than "));
        queryPane.add(Box.createRigidArea(new Dimension(10,0)));
		queryPane.add(catCb2);
		queryPane.add(Box.createRigidArea(new Dimension(10,0)));
		queryPane.add(new JLabel("?"));
        queryPane.add(Box.createRigidArea(new Dimension(10,0)));

		return queryPane;
	}

	/**
	* Creates the button panel with the query and reset buttons in a box layout.
	*
	* @return The constructed button panel.
	*/
	public JPanel createButtonPane() {
		// Set up the panel.
		buttonPane = new JPanel();
		queryBtn = new JButton("Query");

		// Creates and adds the action listeners for the query button and the reset button.
		queryBtn.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parentCS.requestQuery(4, catCb1.getSelectedIndex(), catCb2.getSelectedIndex());
				}
			});
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parentCS.switchContext(-1);
				}
			});

		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		// Add buttons to the panel.
		buttonPane.add(queryBtn);
		buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
		buttonPane.add(resetBtn);

		return buttonPane;
	}

}