package org.linagora.dao.openpaas.form;

public enum FormlyClass {

	INPUT("form-control ng-pristine ng-untouched ng-invalid ng-invalid-required"),
	SELECT("form-control ng-invalid"),
	NUMBER("form-control ng-invalid")
	;
	
	private final String classStr;
	
	private FormlyClass(final String classStr){
		this.classStr = classStr;
	}

	public String getClassStr() {
		return classStr;
	}
 }
