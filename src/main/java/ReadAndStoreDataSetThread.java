import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.*;

public class ReadAndStoreDataSetThread implements Runnable {
	
	private QueryController queryController;
	private String line;
	
	public ReadAndStoreDataSetThread(QueryController qc, String l) {
		queryController = qc;
		line = l;
	}

	public void run() {
		queryController.addToDataSet(line);
	}

}