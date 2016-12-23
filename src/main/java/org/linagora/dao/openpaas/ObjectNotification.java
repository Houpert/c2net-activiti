package org.linagora.dao.openpaas;

public enum ObjectNotification {

	FORM("form");
	
	private String object;

	public String getObject() {
		return object;
	}

	private ObjectNotification(String object) {
		this.object = object;
	}
	
}
