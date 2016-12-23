package org.linagora.dao.openpaas;

public class TargetNotification {

	private ObjectType objectType;
	private String id;
	
	public TargetNotification(ObjectType objectType, String id) {
		super();
		this.objectType = objectType;
		this.id = id;
	}
	
	public ObjectType getObjectType() {
		return objectType;
	}
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
