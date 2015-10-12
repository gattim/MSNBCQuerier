import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

public class UsersViewCatExplicitThread implements Runnable {

	private QueryController queryController;
	private String category;
	private int times;
	private String line;
	private int userCount;
	private boolean match;
	
	public UsersViewCatExplicitThread(QueryController qc, String cat, int numTimes, String l) {
		queryController = qc;
		category = cat;
		times = numTimes;
		line = l;
	}

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