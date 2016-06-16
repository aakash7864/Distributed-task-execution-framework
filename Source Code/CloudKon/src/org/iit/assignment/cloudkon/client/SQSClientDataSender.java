package org.iit.assignment.cloudkon.client;

import java.util.List;

import org.iit.assignment.cloudkon.sqs.SQSManager;

/**
 * <p>
 * Client to send data to SQS Queue
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class SQSClientDataSender implements Runnable {
	List<String> workload;

	public SQSClientDataSender(List<String> workload) {
		this.workload = workload;
	}

	@Override
	public void run() {
		for (String work : workload) {
			// Sending data to Queue
			SQSManager.sendMessage(Client.taskSubmitQueue, Client.taskSubmitQueueUrl, work);
		}
	}

}