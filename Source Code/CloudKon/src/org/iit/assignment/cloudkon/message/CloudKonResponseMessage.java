package org.iit.assignment.cloudkon.message;
public class CloudKonResponseMessage {

	int id;
	int respCode;

	public CloudKonResponseMessage(int id, int respCode) {
		this.id = id;
		this.respCode = respCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

}