package org.iit.assignment.cloudkon.worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.iit.assignment.cloudkon.dynamoDB.DynamoDB;
import org.iit.assignment.cloudkon.sqs.SQSManager;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
/**
 * <p>
 * Worker class for remote back-end worker to get data from SQS Queue
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class Worker {

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			// Remote back-end worker
			DynamoDB.init();
			String taskSubmitQueueName ;
			String taskCompleteQueueName;
			if (args.length == 3) {
				String firstarg=args[0];
				if (firstarg.equals("-s")) {
					taskSubmitQueueName= args[1];
					taskCompleteQueueName=args[2];
					AmazonSQS sqs = SQSManager.getSQSQueue(taskSubmitQueueName);
					String queueUrl = sqs.getQueueUrl(taskSubmitQueueName).getQueueUrl();
					ExecutorService executor = Executors.newFixedThreadPool(8);
					Message message;
					while ((message = SQSManager.getNextMessage(sqs, queueUrl)) != null) {
						// Running Worker Thread
						WorkerThread workerThread = new WorkerThread(taskSubmitQueueName, taskCompleteQueueName);
						executor.execute(workerThread);
					}
					executor.shutdown();
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
	

	
}
