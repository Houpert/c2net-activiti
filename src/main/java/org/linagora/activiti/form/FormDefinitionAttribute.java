package org.linagora.activiti.form;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

public class FormDefinitionAttribute {

	public String key;
	public String type;
	public String placeholder; /*Can be empty*/
	public boolean required; /*Can be empty*/
	public LinkedHashMap<String, String> titleMap;
	
	public FormDefinitionAttribute(String key){
		this.key = key;
	}
	
	public FormDefinitionAttribute(String key, String type) {
		super();
		this.key = key;
		this.type = type;
	}

	public FormDefinitionAttribute(String key, String type, String placeholder) {
		super();
		this.key = key;
		this.type = type;
		this.placeholder = placeholder;
	}

	public FormDefinitionAttribute(String key, String type, String placeholder, boolean required) {
		super();
		this.key = key;
		this.type = type;
		this.placeholder = placeholder;
		this.required = required;
	}
	
	public FormDefinitionAttribute(String key, String type, LinkedHashMap<String, String> titleMap, boolean required) {
		super();
		this.key = key;
		this.type = type;
		this.titleMap = titleMap;
		this.required = required;
	}

	public String generateJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
