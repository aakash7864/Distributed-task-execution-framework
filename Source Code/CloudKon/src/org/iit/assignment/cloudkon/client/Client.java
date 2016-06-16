package org.iit.assignment.cloudkon.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.iit.assignment.cloudkon.message.CloudKonResponseMessage;
import org.iit.assignment.cloudkon.message.CloudKonSleepMessage;
import org.iit.assignment.cloudkon.sqs.SQSManager;
import org.iit.assignment.cloudkon.worker.LocalWorker;

import com.amazonaws.services.sqs.AmazonSQS;

/**
 * <p>
 * Main execution Point of Client for execution of task execution framework.
 * This class run as client program for both local back-end worker and remote
 * back-end worker
 * </p>
 * <p>
 * Running Program for In-memory is very easy all you need to is to run the
 * below command: java -jar CloudKonClient1.0.jar -s local -t 1 -w workload.txt
 * If the client takes –s parameter as “local” it will run the program as
 * in-memory -t is for number of Threads to run as worker -w is for work load
 * file.
 * 
 * To run client for back-end woker java -jar CloudKonClient1.0.jar -s sub con
 * -w workload.txt If the client takes –s parameter takes two Queue first for
 * task submission and second from task completion -w is for work load file.
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class Client {
	static AmazonSQS taskSubmitQueue;
	static AmazonSQS taskCompletedQueue;
	static String taskSubmitQueueUrl;
	static String taskCompletedQueueUrl;
	static String taskSubmitQueueName;
	static String taskCompleteQueueName;

	public static void main(String[] args) {
		if (args.length > 0) {
			// Local back-end worker
			if (args.length == 6) {
				String firstarg = args[0];
				if (firstarg.equals("-s")) {
					String QueueName = args[1];
					if (QueueName.equalsIgnoreCase("local")) {
						System.out.println(QueueName);
					} else {
						System.out.println("Invalid Input");
					}
				} else {
					System.out.println("Invalid Input");
				}
				if (args[4].equalsIgnoreCase("-w")) {
					String workLoadFile = args[5];
					File workFile = new File(workLoadFile);
					if (workFile.exists()) {
						System.out.println("Loaded File sucessfully executing Work");
						ArrayList<String> tasks = readFile(workFile);
						// creating workload
						HashMap<Integer, String> resultMap = new HashMap<Integer, String>();
						ArrayList<CloudKonSleepMessage> workLoad = new ArrayList<CloudKonSleepMessage>();
						for (int i = 0; i < tasks.size(); i++) {
							String task = tasks.get(i);
							String[] msgTokens = task.split(" ");
							workLoad.add(new CloudKonSleepMessage(i, task, Long.valueOf(msgTokens[1])));
							resultMap.put(i, "taskId");
						}
						// Executing local back-end worker
						Long time = runInMemoryJob(Integer.parseInt(args[3]), workLoad, resultMap);
						System.out.println("Task Finshed in " + time + " ms");
					} else {
						System.out.println("No File Found on given Location");
					}
				} else {
					System.out.println("Invalid Input");
				}

			} else if (args.length == 5) {
				// For Remote Back-end Worker
				String firstarg = args[0];
				if (firstarg.equals("-s")) {
					taskSubmitQueueName = args[1];
					taskCompleteQueueName = args[2];

				} else {
					System.out.println("Invalid Input");
				}
				String workarg = args[3];
				if (workarg.equals("-w")) {
					String workLoadFile = args[4];
					File workFile = new File(workLoadFile);
					if (workFile.exists()) {
						System.out.println("Loaded File sucessfully executing Work");
						ArrayList<String> tasks = readFile(workFile);
						// Initializing SQS Queue
						taskSubmitQueue = SQSManager.getSQSQueue(taskSubmitQueueName);
						taskCompletedQueue = SQSManager.getSQSQueue(taskCompleteQueueName);
						taskSubmitQueueUrl = taskSubmitQueue.getQueueUrl(taskSubmitQueueName).getQueueUrl();
						taskCompletedQueueUrl = taskCompletedQueue.getQueueUrl(taskCompleteQueueName).getQueueUrl();
						// Crating workload
						List<String> workLoadList = new ArrayList<>();
						Map<String, String> workLoadmap = new HashMap<>();
						for (int i = 0; i < tasks.size(); i++) {
							String task = tasks.get(i);
							workLoadList.add(i + " " + task);
							workLoadmap.put(i + " " + task, "task");
						}
						// Executing local back-end worker
						Long time = remoteclientJob(workLoadList, workLoadmap);
						System.out.println("Task Finshed in " + time + " ms");
					} else {
						System.out.println("No File Found on given Location");
					}
				} else {
					System.out.println("Invalid Input");
				}

			} else {
				System.out.println("Please provid a vaid input arguments");
			}
		} else {
			System.out.println("Invalid Input");
		}
	}

	/**
	 * Gives list of task
	 * 
	 * @param file
	 *            - workload File
	 * @return List of task present in each line
	 */
	public static ArrayList<String> readFile(File file) {
		ArrayList<String> workloadList = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				workloadList.add(line);
				line = br.readLine();

			}
		} catch (IOException e) {
			System.out.println("Error while Reading File" + e.getMessage());
		}
		return workloadList;
	}

	/**
	 * Run Local back-end worker
	 * 
	 * @param numberOfThread
	 *            -No of Threads of worker
	 * @param workLoad
	 *            - Arraylist of task
	 * @param resultMap
	 *            - Map file to check task completion
	 * @return task completion time in ms
	 */
	private static Long runInMemoryJob(int numberOfThread, ArrayList<CloudKonSleepMessage> workLoad, HashMap<Integer, String> resultMap) {
		// Creating Queue
		BlockingQueue<CloudKonSleepMessage> sharedQueue = new LinkedBlockingQueue<>();
		BlockingQueue<CloudKonResponseMessage> resultQueue = new LinkedBlockingQueue<>();
		// Client consumer to read result
		ClientResultConsumer clientResultConsumer = new ClientResultConsumer(resultQueue, resultMap);
		// Client Sender to send task
		LocalProducer<CloudKonSleepMessage> localProducer = new LocalProducer<>(sharedQueue, workLoad);
		// Creating Thread pool
		Thread[] threadPool = new Thread[numberOfThread];
		Thread client = new Thread(localProducer);
		Thread clientResp = new Thread(clientResultConsumer);
		Long startTime = System.currentTimeMillis();
		// starting Client
		client.start();
		clientResp.start();
		try {
			// making client consumer to wait for 5 ms so that data should be
			// present in result Queue
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Thread pool implementation and running worker
		for (int i = 0; i < numberOfThread; i++) {
			threadPool[i] = new Thread(new LocalWorker<>(sharedQueue, resultQueue));
			threadPool[i].start();
		}
		for (int i = 0; i < numberOfThread; i++) {
			try {
				threadPool[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (resultQueue.size() != 0) {
			// System.out.println(resultQueue.size());
		}
		Long totalTime = (System.currentTimeMillis() - startTime);
		System.out.println("Total Time took is " + totalTime);
		return totalTime;
	}

	/**
	 * Run Remote back-end task
	 * 
	 * @param workLoad
	 *            - Array list of task
	 * @param resultMap
	 *            - Map file to check task completion
	 * @return task completion time in ms
	 */
	private static Long remoteclientJob(List<String> workLoadList, Map<String, String> workLoadmap) {
		// Creating client Thread to submit data to SQS Queue and get reult data
		// form SQS Queue
		SQSClientDataSender clientDataSender = new SQSClientDataSender(workLoadList);
		SQSClientDataReciver clientDataReciver = new SQSClientDataReciver(workLoadmap);
		Thread clientSend = new Thread(clientDataSender);
		Thread clientrecive = new Thread(clientDataReciver);
		Long startTime = System.currentTimeMillis();
		// Starting Client
		clientSend.start();
		clientrecive.start();
		while (workLoadmap.size() == workLoadList.size()) {
			try {
				// Waiting client Receiver thread till data is not present in
				// queue
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (workLoadmap.size() != 0) {
			try {
				// Waiting client Receiver thread till all task are not
				// completed
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
