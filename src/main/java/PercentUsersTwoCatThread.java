import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

/**
* Thread that is ran when the query is searching for the percent of users
* that favor one section over another.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class PercentUsersTwoCatThread implements Runnable {

	/** 
	* Reference to the query controller in order to increment the counter
	* for the number of users.
	*/
	private QueryController queryController;
	
	/** Category that the query is searching for. */
	private String category1;

	/** Category that the query is searching for. */
	private String category2;

	/** Line of text the thread is parsing through. */
	private String line;

	/** Count that tracks which category is favored. */
	private int count;
	
	/**
	* Constructor that instantiates the needed fields and sets count to 0. 
	*
	* @param QueryController Refence to the query controller.
	* @param String Category that the query is searching for.
	* @param String Second category that the query is searching for.
	* @param String Line of text the thread is parsing through.
	*/
	public PercentUsersTwoCatThread(QueryController qc, String cat1, String cat2, String l) {
		queryController = qc;
		category1 = cat1;
		category2 = cat2;
		line = l;
		count = 0;
	}

	/**
	* Run method that is necessary in order to implement Runnable interface.
	* This thread goes through every word in a line that is split on the space character;
	* if it is a match of the first category the counter is incremented and if it matches
	* the second category it is decremented. If count is positive at the end, then 
	* the user favored the first category and if not, the opposite is true.
	*/
	public void run() {
		count = 0;
		for (String view : line.split(" ")) {
			if (view.equals(category1)) {
				count++;
			} else if (view.equals(category2)) {
				count--;
			}
		}

		if (count > 0) {
			queryController.increamentNumUsers();
		}
	}
}