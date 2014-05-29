package com.cyeam.cInterphone.model;

public class Record {
	private int id;
	private String username;
	private int processid;
	private String state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getProcessid() {
		return processid;
	}
	public void setProcessid(int processid) {
		this.processid = processid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
