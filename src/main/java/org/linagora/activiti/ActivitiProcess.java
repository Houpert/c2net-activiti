package org.linagora.activiti;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.Form;
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

	public String taskFormGenerator() throws ExceptionGeneratorActiviti{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		List<Form> listForm = new ArrayList<Form>();
		for(Task task : taskService.createTaskQuery().list()){
			listForm.add(ActivitiFormGenerator.generateFormProperty(task, processEngine.getFormService().getTaskFormData(task.getId())));
		}
		Gson gson = new Gson();
		return gson.toJson(listForm);
	}

}
