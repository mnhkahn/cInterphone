package com.cyeam.cInterphone.model;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8663290597835403021L;
	private Integer id;
	private String name;
	private byte[] avatar;

	private List<String> phones;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
}
