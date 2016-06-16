package org.iit.assignment.cloudkon.worker;

import java.util.concurrent.BlockingQueue;

import org.iit.assignment.cloudkon.message.CloudKonResponseMessage;
import org.iit.assignment.cloudkon.message.CloudKonSleepMessage;

/**
 * <p>
 * Worker class for local back-end worker to get data from Queue
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class LocalWorker<T> implements Runnable {

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
				// getting data from Queue
				message = (CloudKonSleepMessage) inMemoryQueue.take();
				// Processing task
				Thread.sleep(message.getSleepTime());
				// Adding data to Queue
				resultQueue.add(new CloudKonResponseMessage(message.getId(), 0));
				// System.out.println("MessageId " + message.getId() + " 0");
			} catch (InterruptedException e) {
				resultQueue.add(new CloudKonResponseMessage(message.getId(), 1));
				System.out.println("Error While Retriving data from queue");
			}
		}
	}

}