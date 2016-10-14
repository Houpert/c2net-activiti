package org.linagora.activiti.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class SchemaDao {
	public String title;
	public String type;
	
	/*Optional*/
	public String description;
	public String pattern;
	public String minLenght;
	public String format;
	@SerializedName("default")
	public String defaults;
	public List<String> enums; /*schemaform don't need the enum in schema, temporary used a wrong for test*/
	

	public SchemaDao(String title, String type) {
		this.title = title;
		if(type.equals("select"))
			this.type = "string";
		else
			this.type = type;
	}
	
	public SchemaDao(String title, String type, String defaults) {
		if(type.equals("select"))
			this.type = "string";
		else
			this.type = type;
		this.title = title;
		this.defaults = defaults;
	}

	public void addEnums(Map<String, String> map){
		enums = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()){
			enums.add(entry.getValue());
		}
	}

	public String getDefaults() {
		return defaults;
	}
	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}
}
