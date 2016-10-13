package org.linagora.activiti.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Form {

	public String taskId;
	public List<FormDefinition> form = new ArrayList<FormDefinition>();
	public SchemaDefinition schema;
	
	public Form(List<FormDefinition> form, SchemaDefinition schema, String taskId) {
		super();
		this.form = form;
		this.schema = schema;
		this.taskId = taskId;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<FormDefinition> getForm() {
		return form;
	}

	public void setForm(List<FormDefinition> form) {
		this.form = form;
	}

	public SchemaDefinition getSchema() {
		return schema;
	}
	public void setSchema(SchemaDefinition schema) {
		this.schema = schema;
	}
	
	public String generateJson(){
		form.add(FormDefinition.makeSubmitButton());
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
