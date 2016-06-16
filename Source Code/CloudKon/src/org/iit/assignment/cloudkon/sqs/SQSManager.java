package org.iit.assignment.cloudkon.sqs;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

/**
 * <p>
 * SQS Manager Class that connect to SQS Queue to send and receive data from SQS
 * Queue
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class SQSManager {

	public static AmazonSQS getSQSQueue(String queueName) {
		AWSCredentials credentails = null;
		AmazonSQS sqs = null;
		try {
			credentails = new BasicAWSCredentials(SQSData.accessKey, SQSData.secretKey);
			sqs = new AmazonSQSClient(credentails);
			Region region = Region.getRegion(Regions.US_WEST_2);
			sqs.setRegion(region);

			GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl(queueName);
			// System.out.println(getQueueUrlResult.getQueueUrl());
			// CreateQueueResult createQueueResult = sqs.createQueue(queueName);
			// System.out.println(createQueueResult.getQueueUrl());
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			System.out.println("Terminating Application ");
			System.exit(0);
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not " + "being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return sqs;
	}

	public static Message getNextMessage(AmazonSQS sqs, String queueUrl) {
		// Getting data from SQS
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		List<Message> SubmittedTasksQueue = sqs.receiveMessage(receiveMessageRequest).getMessages();
		if (!SubmittedTasksQueue.isEmpty()) {
			for (Message message : SubmittedTasksQueue) {
				// System.out.println("Getting next task...\n");
				return message;
			}
		}
		// System.out.println("No more tasks to be executed...\n");
		return null;
	}

	public static void sendMessage(AmazonSQS sqs, String queueNameUrl, String msg) {
		// putting data to SQS Queue
		sqs.sendMessage(queueNameUrl, msg);

	}
}
