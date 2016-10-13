package org.linagora.activiti.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchemaDao {
	public String title;
	public String type;
	
	/*Optional*/
	public String description;
	public String pattern;
	public String minLenght;
	public String format;
	public String defaults;		/*Maybe Rename to default*/
	public List<String> enums;	/*Maybe Rename to enum*/
	

	public SchemaDao(String title, String type) {
		this.title = title;
		if(type.equals("select"))
			this.type = "string";
		else
			this.type = type;
	}
	
	public void addEnums(Map<String, String> map){
		enums = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()){
			enums.add(entry.getValue());
		}
	}
	
}
