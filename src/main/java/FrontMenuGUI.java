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

	public FrontMenuGUI(ContextSwitcher cs) {
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

		Dimension minSize = new Dimension(10,10);
		Dimension prefSize = new Dimension(10,10);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 10);
		
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

	class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

		/** 
		* Associated version number which is used during deserialization
		* to verify that the sender and receiver of the object have loaded 
		* classes that are compatible.
		*/
		private static final long serialVersionUID = 1L;

		private String _title;

		public ComboBoxRenderer(String title) {
			_title = title;
		}

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