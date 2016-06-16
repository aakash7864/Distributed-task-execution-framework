package org.iit.assignment.cloudkon.worker;

import java.util.List;

import org.iit.assignment.cloudkon.dynamoDB.DynamoDB;
import org.iit.assignment.cloudkon.sqs.SQSManager;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
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
public class WorkerThread implements Runnable {

	private static final String dynamoDBTblName = "CloudKonTask";
	private AmazonSQS taskSubmitQueue = null;
	private AmazonSQS taskCompletedQueue = null;
	private String taskSubmitQueueUrl = null;
	private String taskCompletedQueueUrl = null;

	public WorkerThread(String taskSubmitQueueName, String taskCompleteQueueName) {
		// Initializing SQS Queue
		this.taskSubmitQueue = SQSManager.getSQSQueue(taskSubmitQueueName);
		this.taskCompletedQueue = SQSManager.getSQSQueue(taskCompleteQueueName);
		this.taskSubmitQueueUrl = taskSubmitQueue.getQueueUrl(taskSubmitQueueName).getQueueUrl();
		this.taskCompletedQueueUrl = taskCompletedQueue.getQueueUrl(taskCompleteQueueName).getQueueUrl();
	}

	public void reciveMessage() {
		// Getting data from Queue
		List<Message> messages = taskSubmitQueue.receiveMessage(this.taskSubmitQueueUrl).getMessages();

		// System.out.println(messages.size());
		if (!messages.isEmpty()) {
			for (Message message : messages) {
				// Checking task from DynamoDB
				if (DynamoDB.getDataFromTable(dynamoDBTblName, message.getBody()) == null) {
					DynamoDB.putDataIntoTable(dynamoDBTblName, message.getBody());
					String[] msgTokens = message.getBody().split(" ");
					try {
						// Executing task
						Thread.sleep(Integer.parseInt(msgTokens[2]));
						// Sending data to result SQS Queue
						SQSManager.sendMessage(this.taskCompletedQueue, this.taskCompletedQueueUrl, message.getBody());
					} catch (InterruptedException e) {
						SQSManager.sendMessage(this.taskCompletedQueue, this.taskCompletedQueueUrl, message.getBody());
					}
				}
				String messageReceiptHandle = message.getReceiptHandle();
				taskSubmitQueue.deleteMessage(new DeleteMessageRequest(this.taskSubmitQueueUrl, messageReceiptHandle));
			}
		}
	}

	@Override
	public void run() {
		reciveMessage();
	}
}
