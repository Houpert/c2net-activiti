package org.linagora.activiti.form;

public enum FormPropertyType {

	ENUM("select"),
	STRING("string"),
	INTEGER("number"),
	LONG("number"),
	BOOLEAN("conditional"),
	DATE("date"),
	CUSTOM_TYPE("custom type")
	;
	
	private final String type;
	
	private FormPropertyType(final String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
    public boolean equalsName(String otherType) {
        return (otherType == null) ? false : type.equals(otherType);
    }

    public String toString() {
       return this.type;
    }
	
 }
