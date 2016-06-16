package org.iit.assignment.cloudkon.client;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * Client  to send data to Queue
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class LocalProducer<T> implements Runnable {

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
				// putting data to Queue
				this.inMemoryQueue.put(item);
			} catch (InterruptedException e) {
				System.out.println("Exception While Putting data to Queue" + e.getMessage());
			}
		}

	}

}