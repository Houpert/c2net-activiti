package org.linagora.dao.openpaas;

public enum LevelNotification {

	TRANSIENT("transient"),
	PERSISTANT("persistant"),
	INFORMATION("information");
	
	private String level;

	public String getLevel() {
		return level;
	}

	private LevelNotification(String level) {
		this.level = level;
	}
}
