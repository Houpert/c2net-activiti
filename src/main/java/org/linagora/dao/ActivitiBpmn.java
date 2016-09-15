package org.linagora.dao;

public class ActivitiBpmn {
	
	public String name;
	public String data;

	public ActivitiBpmn(String name, String data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
