package org.linagora.dao.openpaas;

public enum ActionNotification {
	CREATED("created");
	
	private String action;

	public String getAction() {
		return action;
	}

	private ActionNotification(String action) {
		this.action = action;
	}
}
