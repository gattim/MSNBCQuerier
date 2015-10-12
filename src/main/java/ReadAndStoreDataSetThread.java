import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

/**
* Thread that is ran when the program is started to read and store the data file
* into a blocking queue.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class ReadAndStoreDataSetThread implements Runnable {
	
	/** 
	* Reference to the query controller in order to increment the counter
	* for the number of users.
	*/
	private QueryController queryController;

	/** Line of text the thread is parsing through. */
	private String line;
	
	/**
	* Constructor that instantiates the needed fields.
	*
	* @param QueryController Refence to the query controller.
	* @param String Line of text the thread is parsing through.
	*/
	public ReadAndStoreDataSetThread(QueryController qc, String l) {
		queryController = qc;
		line = l;
	}

	/**
	* Run method that is necessary in order to implement Runnable interface.
	* This thread just adds takes data from the file and adds it to the blocking queue.
	*/
	public void run() {
		queryController.addToDataSet(line);
	}

}