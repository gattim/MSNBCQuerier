import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

/**
* Thread that is ran when the query is searching for the number of users that
* have visited a specific topic at least a certain, explicated number of times.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class UsersViewCatExplicitThread implements Runnable {

	/** 
	* Reference to the query controller in order to increment the counter
	* for the number of users.
	*/
	private QueryController queryController;

	/** Category that the query is searching for. */
	private String category;

	/** Minimum number of visits in order for the user to count towards the count. */
	private int times;

	/** Line of text the thread is parsing through. */
	private String line;

	/** Keeps track of the number of times a user visited the category. */
	private int userCount;

	/** Flag that confirms that the user is a match to the conditions */
	private boolean match;
	
	/**
	* Constructor that instantiates the needed fields.
	*
	* @param QueryController Refence to the query controller.
	* @param String Category that the query is searching for.
	* @param int Minimum number of visits in order for the user to count towards the count.
	* @param String Line of text the thread is parsing through.
	*/
	public UsersViewCatExplicitThread(QueryController qc, String cat, int numTimes, String l) {
		queryController = qc;
		category = cat;
		times = numTimes;
		line = l;
	}

	/**
	* Run method that is necessary in order to implement Runnable interface.
	* This thread goes through every word in a line that is split on the space character;
	* if it is a match of the category the counter is incremented and then a check occurs
	* to see if it satisfied the number of vists specified. Even if this is satisfied, the 
	* thread continues to see if the count goes over the specified number of times and exits
	* if that occurs.
	*/
	public void run() {
		userCount = 0;
		match = false;
		for (String view : line.split(" ")) {
			if (view.equals(category)) {
				userCount++;
				if (userCount == times) {
					match = true;
				}
			}
			if (userCount > times) {
				match = false;
				break;
			}
		}
		if (match = true && userCount == times) {
			queryController.increamentNumUsers();
		}
	}
}