package org.linagora.activiti;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.Form;
import org.linagora.activiti.form.Formly;
import org.linagora.dao.ActivitiDAO;
import org.linagora.exception.ExceptionGeneratorActiviti;

import com.google.gson.Gson;

public class ActivitiProcess{

	public void optimisation(){
	}

	public String execution(ActivitiDAO bpmn) throws FileNotFoundException{
		InputStream inputStream = new FileInputStream(bpmn.getFile());

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addInputStream(bpmn.getName(), inputStream).deploy();

		//Start the processus
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(bpmn.getProcessId());
		return pi.getId();
	}

	public String listTaskForm() throws ExceptionGeneratorActiviti{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		List<Form> listForm = new ArrayList<Form>();
		for(Task task : taskService.createTaskQuery().list()){
			List<Formly> formly = ActivitiFormGenerator.generateForm(processEngine.getFormService().getTaskFormData(task.getId()));
			listForm.add(new Form(task.getId(), formly));
		}
		Gson gson = new Gson();
		return gson.toJson(listForm);
	}

	public boolean completeTask(String json) throws ExceptionGeneratorActiviti{
		String keyTask = "taskId";
		Map<String,Object> mapAttribute = new Gson().fromJson(json, Map.class);

		String taskId = (String) mapAttribute.get(keyTask);
		mapAttribute.remove(keyTask);

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();
		try{
			taskService.complete(taskId, mapAttribute);
		}catch(ActivitiObjectNotFoundException e){
			throw new ExceptionGeneratorActiviti("Unable to find the task id");
		}

		return true;
	}
}
