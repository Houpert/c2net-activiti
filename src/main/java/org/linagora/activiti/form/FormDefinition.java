package org.linagora.activiti.form;

import java.util.Map;

import com.google.gson.Gson;

public class FormDefinition {

	public String key;
	public String type;
	public String style;
	public String title;	
	public String placeholder;
	public boolean required = true;
	public Map<String, String> titleMap;

	/*Submit button attribute*/
	
	private static final String SUBMIT_TITLE = "OK";
	private static final String SUBMIT_STYLE = "btn-info";
	private static final String SUBMIT_TYPE = "submit";

	public static FormDefinition makeSubmitButton(){
		FormDefinition fda = new FormDefinition();
		fda.title = SUBMIT_TITLE;
		fda.style = SUBMIT_STYLE;
		fda.type = SUBMIT_TYPE;
		return fda;
	}

	public FormDefinition(){
		super();
	}
	
	public FormDefinition(String key){
		this.key = key;
	}

	public FormDefinition(String key, String type) {
		super();
		this.key = key;
		this.type = type;
		this.placeholder = key;
	}

	/*Constructor for enum*/
	public FormDefinition(String key, String type, Map<String, String> titleMap) {
		super();
		this.key = key;
		this.placeholder = key;
		this.type = type;
		this.titleMap = titleMap;
	}

	public String generateJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
