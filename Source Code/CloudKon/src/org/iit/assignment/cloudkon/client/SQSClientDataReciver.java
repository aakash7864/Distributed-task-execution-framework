package org.iit.assignment.cloudkon.client;

import java.util.Map;

import org.iit.assignment.cloudkon.sqs.SQSManager;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;

/**
 * <p>
 * Client consumer to get data from Result Queue of remote back-end task. It
 * takes Hashmap of task to check all task are completed or not
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
class SQSClientDataReciver implements Runnable {
	Map<String, String> workload;

	public SQSClientDataReciver(Map<String, String> workload) {
		this.workload = workload;
	}

	@Override
	public void run() {
		while (workload.size() != 0) {
			// Getting message from Result SQS queue
			Message message = SQSManager.getNextMessage(Client.taskCompletedQueue, Client.taskCompletedQueueUrl);
			if (message != null) {
				if (workload.get(message.getBody()) != null) {
					// Removing data from hashmap
					workload.remove(message.getBody());
				}
				String messageReceiptHandle = message.getReceiptHandle();
				// deleting data from result SQS Queue
				Client.taskCompletedQueue.deleteMessage(new DeleteMessageRequest(Client.taskCompletedQueueUrl, messageReceiptHandle));
			}

		}
	}
}