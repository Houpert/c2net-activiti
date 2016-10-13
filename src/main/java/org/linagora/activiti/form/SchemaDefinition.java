package org.linagora.activiti.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaDefinition {
	
	private static final String DEFAULT_TYPE = "object";
	public String type;
	public String title;
	
	public Map<String, SchemaDao> properties = new HashMap<String, SchemaDao>();
	public List<String> required = new ArrayList<String>();

	
	public SchemaDefinition(String title) {
		this.title = title;
		this.type = DEFAULT_TYPE;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getRequired() {
		return required;
	}
	public void setRequired(List<String> required) {
		this.required = required;
	}
	public Map<String, SchemaDao> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, SchemaDao> properties) {
		this.properties = properties;
	}
	public SchemaDao addLinkedHashMap(String keyHashMap, SchemaDao schema) {
		required.add(keyHashMap);
		return properties.put(keyHashMap, schema);
	}
}
