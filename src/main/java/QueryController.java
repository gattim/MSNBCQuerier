import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.*;
import java.util.concurrent.*;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
* Controls all the queries that are requested from the context switcher.
* Queriers are executed using a multithreading approach.
*
* @author Marc Gatti
* @version 1.0
* @since 9/7/2015
*/
public class QueryController {

	/** This is what handles the threads that are constructed. */
	private ExecutorService executor;
	
	/** Instatiate so that it can communicate with the context switcher. */
	private ContextSwitcher contextSwitcher;
	
	/** Synchronous data structure that stores the data set. */
	private BlockingQueue<String> dataSet;
	
	/** Synchronous count of the number of users that is used for most of the querys. */
	private AtomicInteger numUsers;

	/** Values for the web page categories in the dataset. */
	private String[] categories = new String[] {
		"Front Page", "News", "Tech", "Local", "Opinion",
		"On-Air", "Misc", "Weather", "MSN-News", "Health",
		"Living", "Business", "MSN-Sports", "Sports", "Summary",
		"BBS", "Travel"
	};

	/**
	* Instantiates the context switcher; creates the data structure for storing the data set;
	* starts taking in the data from the file path.
	*
	* @param ContextSwitcher Passed in for communication purposes.
	*/
	public QueryController(ContextSwitcher cs) {
		contextSwitcher = cs;
		dataSet = new ArrayBlockingQueue<String>(989818);
		readAndStoreDataSet("resources/msnbc990928.txt");
	}

	/**
	* Decides which query was requested to run.Queries for relevent data. Passes results to context switcher to display.
	*
	* @param int Specifies the type of query being executed.
	* @param int Category that is being queried.
	*/
	public void executeQuery(int type, int cat1) {
		double nUsers = (double) getNumUsers(Integer.toString(cat1+1)).get();
		double percent = (nUsers / 989818) * 100;
		DecimalFormat df = new DecimalFormat("#.00");
		String percentFormated = df.format(percent);
		contextSwitcher.popup(percentFormated+"% of users looked at "+categories[cat1]+".");
	}

	/**
	* Decides which query was requested to run.Queries for relevent data. Passes results to context switcher to display.
	*
	* @param int Specifies the type of query being executed.
	* @param int Category that is being queried.
	* @param int A different category that is being queried against.
	*/
	public void executeQuery(int type, int cat1, int cat2) {
		switch (type) {
			case 0:
				getNumUsers(Integer.toString(cat2+1));

				if (cat1 < Integer.MAX_VALUE && numUsers.get() > cat1) {
					contextSwitcher.popup("Yes, "+numUsers.get()+" looked at "+categories[cat2]+".");
				} else {
					contextSwitcher.popup("No, "+numUsers.get()+" looked at "+categories[cat2]+".");
				}
				break;
			case 2:
				int numUsers1 = getNumUsers(Integer.toString(cat1+1)).get();
				readAndStoreDataSet("resources/msnbc990928.txt");
				int numUsers2 = getNumUsers(Integer.toString(cat2+1)).get();
				if (cat1 == cat2) {
					contextSwitcher.popup("Those are the same category.");
				} else if (numUsers1 > numUsers2) {
					contextSwitcher.popup("Yes, "+numUsers1+" number of users looked at "+categories[cat1]
						+"\n and only "+numUsers2+" looked at "+categories[cat2]+".");
				} else {
					contextSwitcher.popup("No, only "+numUsers1+" number of users looked at "+categories[cat1]
						+"\n and "+numUsers2+" looked at "+categories[cat2]+".");
				}
				break;
			case 3:
				getExplicitNumVisits(Integer.toString(cat1+1), cat2);

				if (cat1 < Integer.MAX_VALUE) {
					contextSwitcher.popup(numUsers.get()+" looked at "+categories[cat1]+" "+cat2+" number of times.");
				} else {
					contextSwitcher.popup("0 users looked at "+categories[cat1]+" "+cat2+" number of times.");
				}
				break;
			case 4:
				double nUsers = (double) percentUsersTwoCat(Integer.toString(cat1+1), Integer.toString(cat2+1)).get();
				double percent = (nUsers / 989818) * 100;
				DecimalFormat df = new DecimalFormat("#.00");
			
				if (cat1 == cat2) {
					contextSwitcher.popup("Those are the same category.");
				} else {
					if (percent < 0) {
						percent *= -1;
						contextSwitcher.popup(df.format(percent)+"% of users looked at "+categories[cat2]
						+" more than "+categories[cat1]);
					} else if (percent > 0) {
						contextSwitcher.popup(df.format(percent)+"% of users looked at "+categories[cat1]
						+" more than "+categories[cat2]);
					} else {
						contextSwitcher.popup("Equal percentage of users looked at "+categories[cat1]
						+" and "+categories[cat2]);
					}
				}
				break;
		}
	}

	/**
	* Reads in data from the file, sends them off to threads to add to data structure.
	*
	* @param String The file path of the file that is being read in.
	*/
	private void readAndStoreDataSet(String path) {
		try {
			executor = Executors.newWorkStealingPool();
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				executor.execute(new ReadAndStoreDataSetThread(this, line));
			}

			executor.shutdown();
	  		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	* Querys for the number of users that visited a category.
	*
	* @param String Category that is being queried on.
	* @return Number of users that visited the category.
	*/
	private AtomicInteger getNumUsers(String cat) {
		numUsers = new AtomicInteger(0);

		try {
			executor = Executors.newWorkStealingPool();
			while (dataSet.size() > 0) {
				executor.execute(new GetNumUsersThread(this, cat, dataSet.take()));
			}
			
			executor.shutdown();
	  		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
		
		return numUsers;
	}

	/**
	* Querys for the number of users that visited a category an explicit number of times.
	*
	* @param String Category that is being queried on.
	* @return Number of users that visited the category an explicit number of times.
	*/
	private AtomicInteger getExplicitNumVisits(String cat, int numVisits) {
		numUsers = new AtomicInteger(0);

		try {
			executor = Executors.newWorkStealingPool();
			
			while (dataSet.size() > 0) {
				executor.execute(new UsersViewCatExplicitThread(this, cat, numVisits, dataSet.take()));
			}
			executor.shutdown();
	  		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
		
		return numUsers;
	}

	/**
	* Querys for the percent of users that visited one category over another.
	*
	* @param String Category that is being queried on.
	* @param String Second Category that is being queried on.
	* @return Number of users that visited the category over another that will be converted to percent.
	*/
	private AtomicInteger percentUsersTwoCat(String cat1, String cat2) {
		numUsers = new AtomicInteger(0);

		try {
			executor = Executors.newWorkStealingPool();
			
			while (dataSet.size() > 0) {
				executor.execute(new PercentUsersTwoCatThread(this, cat1, cat2, dataSet.take()));
			}
			executor.shutdown();
	  		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
		
		return numUsers;
	}

	/** Increaments the number of users in the count. */
	public void increamentNumUsers() {
		numUsers.getAndIncrement();
	}

	/** Decreaments the number of users in the count. */
	public void decreamentNumUsers() {
		numUsers.getAndDecrement();
	}

	/**
	* Adds a line to the data set.
	*
	* @param String Line that is being added to the data set.
	*/
	public void addToDataSet(String line) {
		try {
			dataSet.put(line);
		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
	}

	/**
	* Takes from the front of the queue.
	*
	* @return Takes from the front of the queue.
	*/
	public String takeFromDataSet() {
		try {
			return dataSet.take();
		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
  		return "";
	}
}