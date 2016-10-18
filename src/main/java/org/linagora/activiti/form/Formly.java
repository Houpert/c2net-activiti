package org.linagora.activiti.form;

import com.google.gson.Gson;

public class Formly {

	private String key;
	private String type;
	private String defaultValue;
	public TemplateOptions templateOptions;

	public Formly(String key, String type) {
		super();
		this.key = key;
		this.type = type;
	}
	
	public Formly(String key, String type, TemplateOptions templateOptions) {
		this(key, type);
		this.templateOptions = templateOptions;
	}

	public Formly(String key, String type, TemplateOptions templateOptions, String defaultValue) {
		this(key, type, templateOptions);
		if(defaultValue != null)
			this.defaultValue = defaultValue;
	}
	
	public Formly(String key, String type, String defaultValue) {
		this(key, type);
		if(defaultValue != null)
			this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public TemplateOptions getTemplateOptions() {
		return templateOptions;
	}
	public void setTemplateOptions(TemplateOptions templateOptions) {
		this.templateOptions = templateOptions;
	}
	public String generateJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
