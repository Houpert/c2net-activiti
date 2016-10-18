package org.linagora.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.linagora.activiti.form.Formly;
import org.linagora.activiti.form.FormlyType;
import org.linagora.activiti.form.TemplateOptions;
import org.linagora.exception.ExceptionGeneratorActiviti;

public class ActivitiFormGenerator {

	private final static String ENUM_INFORMATION_VALUES = "values";

	public static List<Formly> generateForm(FormData formData) throws ExceptionGeneratorActiviti {
		List<Formly> formlyList= new ArrayList<Formly>();
		for(FormProperty propertyForm : formData.getFormProperties()){
			FormlyType propertyType = getFormType(propertyForm.getType());
			String key = propertyForm.getId();
			String type = propertyType.getTypeFormly();
			TemplateOptions templateOptions = new TemplateOptions(propertyForm, propertyType);

			if(propertyType.equals(FormlyType.DATE) || propertyType.equals(FormlyType.CUSTOM_TYPE))
				throw new ExceptionGeneratorActiviti("Unable to parse the Form for Property");
			else if(propertyType.equals(FormlyType.ENUM)){
				templateOptions = makeTemplateOption(templateOptions, propertyForm);
			}
			
			System.out.println("##"+propertyForm.getValue());
			formlyList.add(new Formly(key, type, templateOptions, propertyForm.getValue()));
		}
		return formlyList;
	}

	private static FormlyType getFormType(FormType type) {
		if(type != null)
			return FormlyType.valueOf(type.getName().toUpperCase());
		return FormlyType.CUSTOM_TYPE;
	}

	@SuppressWarnings("unchecked")
	private static TemplateOptions makeTemplateOption(TemplateOptions templateOptions, FormProperty propertyForm) throws ExceptionGeneratorActiviti {
		try {
			Map<String, String> map = (Map<String, String>)propertyForm.getType().getInformation(ENUM_INFORMATION_VALUES);
			for (Map.Entry<String, String> entry : map.entrySet())
				templateOptions.addOption(entry.getValue(), entry.getKey());
			return templateOptions;
		}catch(Exception e){
			throw new ExceptionGeneratorActiviti("Unable to cast enum value");
		}
	}
}
