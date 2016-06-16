package org.iit.assignment.cloudkon.client;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import org.iit.assignment.cloudkon.message.CloudKonResponseMessage;

/**
 * <p>
 * Client consumer to get data from Result Queue of local back-end task. It
 * takes Hashmap of task to check all task are completed or not
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class ClientResultConsumer implements Runnable {

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
				// Getting task from Queue
				CloudKonResponseMessage message = resultQueue.take();
				if (this.taskCompletionChecker.get(message.getId()) != null) {
					// Removing completed task from hashmap
					this.taskCompletionChecker.remove(message.getId());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}