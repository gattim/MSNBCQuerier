import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

/**
* Thread that is ran when the query is searching for the number of users that
* have visited a specific topic.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class GetNumUsersThread implements Runnable {

	/** 
	* Reference to the query controller in order to increment the counter
	* for the number of users.
	*/
	private QueryController queryController;
	
	/** Category that the query is searching for. */
	private String category;
	
	/** Line of text the thread is parsing through. */
	private String line;
	
	/**
	* Constructor that instantiates the needed fields.
	*
	* @param QueryController Refence to the query controller.
	* @param String Category that the query is searching for.
	* @param String Line of text the thread is parsing through.
	*/
	public GetNumUsersThread(QueryController qc, String cat, String l) {
		queryController = qc;
		category = cat;
		line = l;
	}

	/**
	* Run method that is necessary in order to implement Runnable interface.
	* This thread goes through every word in a line that is split on the space character;
	* if it is a match of the category the counter is incremented and there is no reaseon
	* to continue parsing the line.
	*/
	public void run() {
		for (String view : line.split(" ")) {
			if (view.equals(category)) {
				queryController.increamentNumUsers();
				break;
			}	
		}
	}
}