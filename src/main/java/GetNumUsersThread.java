import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

public class GetNumUsersThread implements Runnable {

	private QueryController queryController;
	private String category;
	private String line;
	
	public GetNumUsersThread(QueryController qc, String cat, String l) {
		queryController = qc;
		category = cat;
		line = l;
	}

	public void run() {
		for (String view : line.split(" ")) {
			if (view.equals(category)) {
				queryController.increamentNumUsers();
				break;
			}	
		}
	}
}