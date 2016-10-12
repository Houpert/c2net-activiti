package org.linagora.activiti.form;

import java.util.List;

public class FormDefinition {

	public List<FormDefinitionAttribute> formAttribute;		/* if '*' print all Schema */
	public String formType;
	public String formStyle;	/*Can be empty*/
	public String formTitle;

	
	public List<FormSchemaDefinition> propertiesSchema;
	public List<String> required;
	public String schemaType;
	public String schemaTitle;

}
