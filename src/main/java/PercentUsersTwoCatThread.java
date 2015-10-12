import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

public class PercentUsersTwoCatThread implements Runnable {

	private QueryController queryController;
	private String category1;
	private String category2;
	private String line;
	private int count;
	
	public PercentUsersTwoCatThread(QueryController qc, String cat1, String cat2, String l) {
		queryController = qc;
		category1 = cat1;
		category2 = cat2;
		line = l;
		count = 0;
	}

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