import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.*;
import java.util.concurrent.*;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryController {

	private ExecutorService executor;
	private ContextSwitcher contextSwitcher;
	private BlockingQueue<String> dataSet;
	private AtomicInteger numUsers;
	private AtomicInteger dataSetSize;

	/** Values for the web page categories in the dataset. */
	private String[] categories = new String[] {
		"Front Page", "News", "Tech", "Local", "Opinion",
		"On-Air", "Misc", "Weather", "MSN-News", "Health",
		"Living", "Business", "MSN-Sports", "Sports", "Summary",
		"BBS", "Travel"
	};

	public QueryController(ContextSwitcher cs) {
		contextSwitcher = cs;
		dataSet = new ArrayBlockingQueue<String>(989818);
		readAndStoreDataSet("resources/msnbc990928.txt");
	}

	public void executeQuery(int type, int cat1) {
		double nUsers = (double) getNumUsers(Integer.toString(cat1+1)).get();
		double percent = (nUsers / 989818) * 100;
		DecimalFormat df = new DecimalFormat("#.00");
		String percentFormated = df.format(percent);
		contextSwitcher.popup(percentFormated+"% of users looked at "+categories[cat1]+".");
	}

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

	private void readAndStoreDataSet(String path) {
		
		dataSetSize = new AtomicInteger(0);

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

	public void increamentNumUsers() {
		numUsers.getAndIncrement();
	}

	public void decreamentNumUsers() {
		numUsers.getAndDecrement();
	}

	public void addToDataSet(String line) {
		try {
			dataSet.put(line);
		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
	}

	public String takeFromDataSet() {
		try {
			return dataSet.take();
		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
  		return "";
	}
}