package org.iit.assignment.cloudkon.message;

/**
 * <p>
 * Request Message class used for sending data to local back-end worker
 * </p>
 * 
 * @author Aishwarya Anand (A20331867)
 * @mail aanand12@hawk.iit.edu
 * 
 */
public class CloudKonSleepMessage {

	private int id;
	private String data;
	private Long sleepTime;

	public CloudKonSleepMessage(int id, String data, Long sleepTime) {

		this.id = id;
		this.data = data;
		this.sleepTime = sleepTime;
	}

	public Long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}