package org.linagora.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.task.Task;
import org.linagora.dao.openpaas.form.Formly;
import org.linagora.dao.openpaas.form.FormlyType;
import org.linagora.dao.openpaas.form.TemplateOptions;
import org.linagora.exception.ExceptionGeneratorActiviti;

public class ActivitiFormGenerator {

	private final static String ENUM_INFORMATION_VALUES = "values";
	private final static String TASK_ATTRIBUTE = "taskId";
	private final static String TASK_VALUE_HIDDEN_FORM = ""; /*Empty for not appear in form*/


	public static List<Formly> generateForm(Task task, FormData formData) throws ExceptionGeneratorActiviti {
		List<Formly> formlyList= new ArrayList<Formly>();

		for(FormProperty propertyForm : formData.getFormProperties()){
			FormlyType propertyType = getFormType(propertyForm.getType());
			TemplateOptions templateOptions = new TemplateOptions(propertyForm, propertyType);

			if(propertyType.equals(FormlyType.DATE) || propertyType.equals(FormlyType.CUSTOM_TYPE))
				throw new ExceptionGeneratorActiviti("Unable to parse the Form for Property");	/*TODO NOT YET SUPPORTED*/
			else if(propertyType.equals(FormlyType.ENUM)){
				templateOptions = makeTemplateOptions(templateOptions, propertyForm);
			}
			formlyList.add(new Formly(propertyForm.getId(), propertyType.getTypeFormly(), templateOptions, propertyForm.getValue()));
		}

		TemplateOptions templateOptionsHidden = new TemplateOptions(TASK_VALUE_HIDDEN_FORM, FormlyType.HIDDEN.getTypeFormly());
		templateOptionsHidden.setPlaceholder(task.getName());	/*Used for have some information*/
		formlyList.add(new Formly(TASK_ATTRIBUTE, FormlyType.STRING.getTypeFormly(), templateOptionsHidden, task.getId()));

		return formlyList;
	}

	private static FormlyType getFormType(FormType type) {
		if(type != null)
			return FormlyType.valueOf(type.getName().toUpperCase());
		return FormlyType.CUSTOM_TYPE;
	}

	private static TemplateOptions makeTemplateOptions(TemplateOptions templateOptions, FormProperty propertyForm) throws ExceptionGeneratorActiviti {
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
