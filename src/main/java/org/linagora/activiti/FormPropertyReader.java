package org.linagora.activiti;

import java.util.LinkedHashMap;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.FormDefinition;
import org.linagora.activiti.form.FormDefinitionAttribute;
import org.linagora.activiti.form.FormPropertyType;
import org.linagora.exception.ExceptionGeneratorActiviti;

public class FormPropertyReader {

	private final static String ENUM_INFORMATION_VALUES = "values";

	public static FormDefinition generateFormProperty(Task task, FormData formData ) throws ExceptionGeneratorActiviti {
		// TODO Auto-generated method stub
		FormDefinition formDefinition = new FormDefinition();

		for(FormProperty prop : formData.getFormProperties()){
			FormPropertyType propType;
			if(prop.getType() != null)
				propType = FormPropertyType.valueOf(prop.getType().getName().toUpperCase());
			else
				propType = FormPropertyType.CUSTOM_TYPE;

			if(propType.equals(FormPropertyType.DATE) || propType.equals(FormPropertyType.CUSTOM_TYPE))
				throw new ExceptionGeneratorActiviti("Unable to parse the Form for Property");

			FormDefinitionAttribute fda;
			if(propType.equals(FormPropertyType.ENUM)){
				Object enumList = prop.getType().getInformation(ENUM_INFORMATION_VALUES);

				fda = new FormDefinitionAttribute(prop.getName(), propType.getType(),
						checkCast(enumList), false);
			}else {
				fda = new FormDefinitionAttribute(prop.getName(), propType.getType());
			}

			System.out.println(fda.generateJson());
		}

		return formDefinition;
	}

	@SuppressWarnings("unchecked")
	private static LinkedHashMap<String, String> checkCast(Object enumList) throws ExceptionGeneratorActiviti {
		try {
			return (LinkedHashMap<String, String>) enumList;
		}catch(Exception e){
			throw new ExceptionGeneratorActiviti("Unable to cast enum value");
		}
	}
}
