package org.linagora.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.Form;
import org.linagora.activiti.form.FormDefinition;
import org.linagora.activiti.form.FormPropertyType;
import org.linagora.activiti.form.SchemaDao;
import org.linagora.activiti.form.SchemaDefinition;
import org.linagora.exception.ExceptionGeneratorActiviti;

public class ActivitiFormGenerator {

	private final static String ENUM_INFORMATION_VALUES = "values";

	public static Form generateFormProperty(Task task, FormData formData ) throws ExceptionGeneratorActiviti {
		List<FormDefinition> formDef = new ArrayList<FormDefinition>();
		SchemaDefinition schemaDef = new SchemaDefinition(task.getName());

		for(FormProperty propertyForm : formData.getFormProperties()){
			FormPropertyType propertyType = getFormType(propertyForm.getType());
			SchemaDao schemaData = new SchemaDao(propertyForm.getId(), propertyType.getType());
			//TODO Manage Form property and constraint in schema

			if(propertyType.equals(FormPropertyType.DATE) || propertyType.equals(FormPropertyType.CUSTOM_TYPE))
				throw new ExceptionGeneratorActiviti("Unable to parse the Form for Property");
			else if(propertyType.equals(FormPropertyType.ENUM)){
				formDef.add(makeFormDefEnum(propertyForm, propertyType));
				schemaData.addEnums(checkValidityCast(propertyForm));
			}else {
				formDef.add(new FormDefinition(propertyForm.getId(), propertyType.getType()));
			}
			
			schemaDef.addLinkedHashMap(propertyForm.getId(), schemaData);
		}

		Form formDefinition = new Form(formDef, schemaDef, task.getId());
		return formDefinition;
	}

	private static FormDefinition makeFormDefEnum(FormProperty propertyForm, FormPropertyType propertyType) throws ExceptionGeneratorActiviti {
		return new FormDefinition(propertyForm.getId(), propertyType.getType(), checkValidityCast(propertyForm), false);
	}

	private static FormPropertyType getFormType(FormType type) {
		if(type != null)
			return FormPropertyType.valueOf(type.getName().toUpperCase());
		return FormPropertyType.CUSTOM_TYPE;
	}

	
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> checkValidityCast(FormProperty propertyForm) throws ExceptionGeneratorActiviti {
		try {
			Object enumList = propertyForm.getType().getInformation(ENUM_INFORMATION_VALUES);
			return (Map<String, String>) enumList;
		}catch(Exception e){
			throw new ExceptionGeneratorActiviti("Unable to cast enum value");
		}
	}
}
