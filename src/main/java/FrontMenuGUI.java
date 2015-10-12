import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
* This is the main menu of the program that just contains a combobox
* for the user to decide what query they want to execute.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class FrontMenuGUI {

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
		"Are there more than ____ users who looked at X?",
		"What percent of users looked at X?",
		"Are there more users who looked at X than Y?",
		"How many users viewed X ____ number of times?",
		"What percent of users looked at X more than Y?"
	};

	/** Panel that holds just the combobox for selected the query type. */
	private JPanel queryTypePane;

	/** Combobox that holds the different possible queries. */
	private JComboBox<String> queryCb;

	/** Reference to parent context switcher. */
	private ContextSwitcher parentCS;

	/**
	* Constructor that strores a reference to the parent context switcher.
	*
	* @param cs Parent context switcher.
	*/
	public FrontMenuGUI(ContextSwitcher cs) {
		parentCS = cs;
	}

	/**
	* Creates the content panel for the GUI. It first creates the bottom
	* level panel and then builds the query panel on top of it.
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
		queryTypePane = new JPanel();

		// Defines the combobox and the action listener that switches the context based on selection.
		queryCb = new JComboBox<String>(queryTypes);
		queryCb.setRenderer(new ComboBoxRenderer("Select your query."));
		queryCb.setSelectedIndex(-1);
		queryCb.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parentCS.switchContext(queryCb.getSelectedIndex());
				}
			});

        queryTypePane.setLayout(new BoxLayout(queryTypePane, BoxLayout.X_AXIS));

        queryTypePane.add(Box.createRigidArea(new Dimension(10,0)));
		queryTypePane.add(queryCb);
		queryTypePane.add(Box.createRigidArea(new Dimension(10,0)));

		return queryTypePane;
	}

	/**
	* Inner class for a combobox renderer that allows the combobox to have a default selected title.
	*
	* @author Marc Gatti
	* @version 1.0
	* @since 10/9/2015
	*/
	class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

		/** 
		* Associated version number which is used during deserialization
		* to verify that the sender and receiver of the object have loaded 
		* classes that are compatible.
		*/
		private static final long serialVersionUID = 1L;

		/** String field that stores the title that the combobox will have. */
		private String _title;

		/**  
		* Constructor that just stores the title.
		*
		* @param String The title that will be the default for the combobox.
		*/
		public ComboBoxRenderer(String title) {
			_title = title;
		}

		/*
		* Sets the title for the combobox and returns a reference to this classes object
		*
		* @param JList<? extends Object> List of objects stored in the combobox.
		* @param Object Reference data for parameter list's data.
		* @param int Index in parameter list.
		* @param boolean True if it is silected in the list. False if it is not.
		* @param boolean True if it is focused in the list. False if it is not.
		* @return The reference to this object. 
		*/
		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean hasFocus) {
			
			if (index == -1 && value == null) {
				setText(_title);
			} else {
				setText(value.toString());
			}

			return this;
		}
	}

}