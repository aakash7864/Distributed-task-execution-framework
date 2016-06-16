package org.iit.assignment.cloudkon.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * Performance Class to run all kind of experiment for Local back-end worker.
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class PerformanceInMem {
//Few calulation are incorrect I have corrected it in document 
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("perf")) {
				if (args[1].equals("1")) {
					System.out.println("Starting ThroughPut Experiment with No of worker Threads 1");
					throughPutLoadSleep0Thread(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting ThroughPut Experiment with No of worker Threads 2");
					throughPutLoadSleep0Thread(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting ThroughPut Experiment with No of worker Threads 4");
					throughPutLoadSleep0Thread(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting ThroughPut Experiment with No of worker Threads 8");
					throughPutLoadSleep0Thread(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting ThroughPut Experiment with No of worker Threads 16");
					throughPutLoadSleep0Thread(16);
				}
			} else if (args[0].equalsIgnoreCase("sleep")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 1");
					efficencySleep10Thread(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 2");
					efficencySleep10Thread(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 4");
					efficencySleep10Thread(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 8");
					efficencySleep10Thread(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 16");
					efficencySleep10Thread(16);
				}
			} else if (args[0].equalsIgnoreCase("sleep1")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 1");
					efficencySleep1000Thread(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 2");
					efficencySleep1000Thread(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 4");
					efficencySleep1000Thread(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 8");
					efficencySleep1000Thread(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 16");
					efficencySleep1000Thread(16);
				}
			} else if (args[0].equalsIgnoreCase("sleep10")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 1");
					efficencySleep10000Thread(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 2");
					efficencySleep10000Thread(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 4");
					efficencySleep10000Thread(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 8");
					efficencySleep10000Thread(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 16");
					efficencySleep10000Thread(16);
				}
			}
		}
		// efficencySleep10Thread(1);
		System.exit(0);
	}

	private static Long runInMemoryJob(int numberOfThread, ArrayList<CloudKonSleepMessage> workLoad, HashMap<Integer, String> resultMap) {
		BlockingQueue<CloudKonSleepMessage> sharedQueue = new LinkedBlockingQueue<>();
		BlockingQueue<CloudKonResponseMessage> resultQueue = new LinkedBlockingQueue<>();
		ClientResultConsumer clientResultConsumer = new ClientResultConsumer(resultQueue, resultMap);
		LocalProducer<CloudKonSleepMessage> localProducer = new LocalProducer<>(sharedQueue, workLoad);
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
		Thread[] threadPool = new Thread[numberOfThread];

		Thread client = new Thread(localProducer);
		Thread clientResp = new Thread(clientResultConsumer);
		Long startTime = System.currentTimeMillis();
		client.start();
		clientResp.start();

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < numberOfThread; i++) {
			threadPool[i] = new Thread(new LocalWorker<>(sharedQueue, resultQueue));
			threadPool[i].start();
		}
		// while (!sharedQueue.isEmpty()) {
		// LocalWorker<CloudKonSleepMessage> localWorker = new
		// LocalWorker<>(sharedQueue, resultQueue);
		// executor.execute(localWorker);
		// }
		// System.out.println("Sutting Down executor");
		// executor.shutdown();
		for (int i = 0; i < numberOfThread; i++) {
			// threadPool[i] = new Thread(new LocalWorker<>(sharedQueue,
			// resultQueue));
			try {
				threadPool[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (resultQueue.size() != 0) {
			// System.out.println(resultQueue.size());
		}

		// executor.shutdownNow();
		Long totalTime = (System.currentTimeMillis() - startTime);
		System.out.println("Total Time took is " + totalTime);
		return totalTime;
	}

	public static void throughPutLoadSleep0Thread(int numberOfThread) {
		// System.out.println("Performace for Sleep 0 task ");
		ArrayList<CloudKonSleepMessage> workLoad = new ArrayList<CloudKonSleepMessage>();
		HashMap<Integer, String> resultMap = new HashMap<Integer, String>();
		for (int i = 0; i < 100000; i++) {
			workLoad.add(new CloudKonSleepMessage(i, "sleep 0", 0l));
			resultMap.put(i, "taskId");
		}
		Long totaltime = runInMemoryJob(numberOfThread, workLoad, resultMap);
		double throughPut = (double) (Double.valueOf(100000) / totaltime);
		System.out.println("ThroughPut  for Number Of threads " + numberOfThread + "is " + String.valueOf(throughPut));
	}

	public static void efficencySleep10Thread(int numberOfThread) {
		// System.out.println("Performace for Sleep 0 task ");
		ArrayList<CloudKonSleepMessage> workLoad = new ArrayList<CloudKonSleepMessage>();
		HashMap<Integer, String> resultMap = new HashMap<Integer, String>();
		for (int i = 0; i < 1000 * numberOfThread; i++) {
			workLoad.add(new CloudKonSleepMessage(i, "sleep 10", 10l));
			resultMap.put(i, "taskId");
		}
		Long totaltime = runInMemoryJob(numberOfThread, workLoad, resultMap);
		double efficency = Double.valueOf((10 * 1000 * numberOfThread)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThread + "is " + String.valueOf(efficency * 100));
	}

	public static void efficencySleep1000Thread(int numberOfThread) {
		// System.out.println("Performace for Sleep 0 task ");
		ArrayList<CloudKonSleepMessage> workLoad = new ArrayList<CloudKonSleepMessage>();
		HashMap<Integer, String> resultMap = new HashMap<Integer, String>();
		for (int i = 0; i < 100 * numberOfThread; i++) {
			workLoad.add(new CloudKonSleepMessage(i, "sleep 1000", 1000l));
			resultMap.put(i, "taskId");
		}
		Long totaltime = runInMemoryJob(numberOfThread, workLoad, resultMap);
		double efficency = Double.valueOf((100 * 1000 * numberOfThread)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThread + "is " + String.valueOf(efficency * 100));
	}

	public static void efficencySleep10000Thread(int numberOfThread) {
		// System.out.println("Performace for Sleep 0 task ");
		ArrayList<CloudKonSleepMessage> workLoad = new ArrayList<CloudKonSleepMessage>();
		HashMap<Integer, String> resultMap = new HashMap<Integer, String>();
		for (int i = 0; i < 10 * numberOfThread; i++) {
			workLoad.add(new CloudKonSleepMessage(i, "sleep 10000", 10000l));
			resultMap.put(i, "taskId");
		}
		Long totaltime = runInMemoryJob(numberOfThread, workLoad, resultMap);
		double efficency = Double.valueOf((10000 * 10 * numberOfThread)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThread + "is " + String.valueOf(efficency * 100));
	}
}

class ClientResultConsumer implements Runnable {

	BlockingQueue<CloudKonResponseMessage> resultQueue;
	HashMap<Integer, String> taskCompletionChecker;

	public ClientResultConsumer(final BlockingQueue<CloudKonResponseMessage> resultQueue, final HashMap<Integer, String> work) {
		this.resultQueue = resultQueue;
		this.taskCompletionChecker = work;
	}

	@Override
	public void run() {
		while (this.taskCompletionChecker.size() != 0) {
			try {
				CloudKonResponseMessage message = resultQueue.take();
				if (this.taskCompletionChecker.get(message.getId()) != null) {
					// System.out.println("removing" + message.getId());
					this.taskCompletionChecker.remove(message.getId());

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

class LocalProducer<T> implements Runnable {

	private BlockingQueue<T> inMemoryQueue;
	List<T> work;

	// private BlockingQueue<T> resultQueue;

	public LocalProducer(final BlockingQueue<T> inMemoryQueue, final List<T> work) {
		this.inMemoryQueue = inMemoryQueue;
		this.work = work;
	}

	@Override
	public void run() {
		for (T item : work) {
			try {
				this.inMemoryQueue.put(item);
			} catch (InterruptedException e) {
				System.out.println("Exception While Putting data to Queue" + e.getMessage());
			}
		}

	}

}

class CloudKonResponseMessage {

	int id;
	int respCode;

	public CloudKonResponseMessage(int id, int respCode) {
		this.id = id;
		this.respCode = respCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

}

class CloudKonSleepMessage {

	private int id;
	private String data;
	private Long sleepTime;

	public CloudKonSleepMessage(int id, String data, Long sleepTime) {

		this.id = id;
		this.data = data;
		this.sleepTime = sleepTime;
	}

	public Long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}

class LocalWorker<T> implements Runnable {

	private BlockingQueue<T> inMemoryQueue;
	private BlockingQueue<CloudKonResponseMessage> resultQueue;

	public LocalWorker(BlockingQueue<T> inMemoryQueue, BlockingQueue<CloudKonResponseMessage> resultQueue) {
		this.inMemoryQueue = inMemoryQueue;
		this.resultQueue = resultQueue;
	}

	@Override
	public void run() {
		while (!inMemoryQueue.isEmpty()) {
			CloudKonSleepMessage message = null;
			try {

				message = (CloudKonSleepMessage) inMemoryQueue.take();
				// System.out.println("MessageId " + message.getId() + "  " +
				// message.getSleepTime());
				Thread.sleep(message.getSleepTime());
				resultQueue.add(new CloudKonResponseMessage(message.getId(), 0));
				// System.out.println("MessageId " + message.getId() + " 0");
			} catch (InterruptedException e) {
				resultQueue.add(new CloudKonResponseMessage(message.getId(), 1));
				System.out.println("Error While Retriving data from queue");
			}
		}
	}

}