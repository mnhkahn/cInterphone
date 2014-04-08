package com.cyeam.cInterphone.model;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8663290597835403021L;
	private String name;
	private List<String> phones;

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
}
