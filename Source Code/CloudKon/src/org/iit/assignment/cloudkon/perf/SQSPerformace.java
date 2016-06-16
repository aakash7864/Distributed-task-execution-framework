package org.iit.assignment.cloudkon.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iit.assignment.cloudkon.sqs.SQSManager;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;

/**
 * <p>
 * Performance Class to run all kind of experiment for remote back-end worker.
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class SQSPerformace {
	static String taskSubmitQueueName;
	static String taskCompleteQueueName;
	static AmazonSQS taskSubmitQueue;
	static AmazonSQS taskCompletedQueue;
	static String taskSubmitQueueUrl;
	static String taskCompletedQueueUrl;

	// Few calculation are incorrect I have corrected it in document
	public static void main(String[] args) {
		taskSubmitQueueName = "SubQueue";
		taskCompleteQueueName = "ComQueue";
		taskSubmitQueue = SQSManager.getSQSQueue(taskSubmitQueueName);
		taskCompletedQueue = SQSManager.getSQSQueue(taskCompleteQueueName);
		taskSubmitQueueUrl = taskSubmitQueue.getQueueUrl(taskSubmitQueueName).getQueueUrl();
		taskCompletedQueueUrl = taskCompletedQueue.getQueueUrl(taskCompleteQueueName).getQueueUrl();

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("perf")) {
				thorughPutWorkload();
			} else if (args[0].equalsIgnoreCase("sleep")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 1");
					EfficencyWorkloadSleep(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 2");
					EfficencyWorkloadSleep(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 4");
					EfficencyWorkloadSleep(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 8");
					EfficencyWorkloadSleep(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 ms No of worker Threads 16");
					EfficencyWorkloadSleep(16);
				}
			} else if (args[0].equalsIgnoreCase("sleep1")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 1");
					EfficencyWorkloadSleep1(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 2");
					EfficencyWorkloadSleep1(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 4");
					EfficencyWorkloadSleep1(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 8");
					EfficencyWorkloadSleep1(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 1 sec No of worker Threads 16");
					EfficencyWorkloadSleep1(16);
				}
			} else if (args[0].equalsIgnoreCase("sleep10")) {
				if (args[1].equals("1")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 1");
					EfficencyWorkloadSleep10(1);
				} else if (args[1].equals("2")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 2");
					EfficencyWorkloadSleep10(2);
				} else if (args[1].equals("4")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 4");
					EfficencyWorkloadSleep10(4);
				} else if (args[1].equals("8")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 8");
					EfficencyWorkloadSleep10(8);
				} else if (args[1].equals("16")) {
					System.out.println("Starting Efficency Experiment with Sleep 10 sec No of worker Threads 16");
					EfficencyWorkloadSleep10(16);
				}
			}
		}
		// thorughPutWorkload();
		// efficencySleep10Thread(1);
		// System.exit(0);

	}

	private static void thorughPutWorkload() {
		List<String> workLoadList = new ArrayList<>();
		Map<String, String> workLoadmap = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			workLoadList.add(i + " " + "sleep 0");
			workLoadmap.put(i + " " + "sleep 0", "task");
		}
		Long totaltime = clientJob(workLoadList, workLoadmap);
		double throughPut = (double) (Double.valueOf(10000) / totaltime);
		System.out.println("ThroughPut is " + String.valueOf(throughPut));
	}

	private static void EfficencyWorkloadSleep(int numberOfThreads) {
		List<String> workLoadList = new ArrayList<>();
		Map<String, String> workLoadmap = new HashMap<>();
		for (int i = 0; i < 1000 * numberOfThreads; i++) {
			workLoadList.add(i + " " + "sleep 10");
			workLoadmap.put(i + " " + "sleep 10", "task");
		}
		Long totaltime = clientJob(workLoadList, workLoadmap);
		double efficency = Double.valueOf((10 * 1000 * numberOfThreads)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThreads + "is " + String.valueOf(efficency * 100));
	}

	private static void EfficencyWorkloadSleep1(int numberOfThread) {
		List<String> workLoadList = new ArrayList<>();
		Map<String, String> workLoadmap = new HashMap<>();
		for (int i = 0; i < 100 * numberOfThread; i++) {
			workLoadList.add(i + " " + "sleep 1000");
			workLoadmap.put(i + " " + "sleep 1000", "task");
		}
		Long totaltime = clientJob(workLoadList, workLoadmap);
		double efficency = Double.valueOf((100 * 1000 * numberOfThread)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThread + "is " + String.valueOf(efficency * 100));
	}

	private static void EfficencyWorkloadSleep10(int numberOfThread) {
		List<String> workLoadList = new ArrayList<>();
		Map<String, String> workLoadmap = new HashMap<>();
		for (int i = 0; i < 10 * numberOfThread; i++) {
			workLoadList.add(i + " " + "sleep 10000");
			workLoadmap.put(i + " " + "sleep 10000", "task");
		}
		Long totaltime = clientJob(workLoadList, workLoadmap);
		double efficency = Double.valueOf((10000 * 10 * numberOfThread)) / totaltime;
		System.out.println("Efficency  for Number Of threads " + numberOfThread + "is " + String.valueOf(efficency * 100));
	}

	private static Long clientJob(List<String> workLoadList, Map<String, String> workLoadmap) {
		SQSClientDataSender clientDataSender = new SQSClientDataSender(workLoadList);
		SQSClientDataReciver clientDataReciver = new SQSClientDataReciver(workLoadmap);
		Thread clientSend = new Thread(clientDataSender);
		Thread clientrecive = new Thread(clientDataReciver);
		Long startTime = System.currentTimeMillis();
		clientSend.start();
		clientrecive.start();
		while (workLoadmap.size() == workLoadList.size()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (workLoadmap.size() != 0) {
			try {
				Thread.sleep(5000);
				// System.out.println(workLoadmap.size());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long timeTook = (System.currentTimeMillis() - startTime);
		System.out.println("Total time took " + timeTook);
		return timeTook;
	}

}

class SQSClientDataSender implements Runnable {
	List<String> workload;

	public SQSClientDataSender(List<String> workload) {
		this.workload = workload;
	}

	@Override
	public void run() {
		for (String work : workload) {
			SQSManager.sendMessage(SQSPerformace.taskSubmitQueue, SQSPerformace.taskSubmitQueueUrl, work);
		}
	}

}

class SQSClientDataReciver implements Runnable {
	Map<String, String> workload;

	public SQSClientDataReciver(Map<String, String> workload) {
		this.workload = workload;
	}

	@Override
	public void run() {
		while (workload.size() != 0) {
			Message message = SQSManager.getNextMessage(SQSPerformace.taskCompletedQueue, SQSPerformace.taskCompletedQueueUrl);
			if (message != null) {
				// System.out.println(message.getBody());
				if (workload.get(message.getBody()) != null) {
					// System.out.println("Removing"+ message.getBody());
					workload.remove(message.getBody());
				}
				String messageReceiptHandle = message.getReceiptHandle();
				SQSPerformace.taskCompletedQueue.deleteMessage(new DeleteMessageRequest(SQSPerformace.taskCompletedQueueUrl, messageReceiptHandle));
			}

		}
	}

}