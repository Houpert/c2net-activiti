package org.linagora.dao.openpaas;

public enum ObjectType {

	USER("user"),
	COMMUNITY("community");
	
	private String objectType;

	public String getObjectType() {
		return objectType;
	}

	private ObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	
}
