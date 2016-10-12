package org.linagora.activiti;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.linagora.dao.ActivitiDAO;
import org.linagora.exception.ExceptionGeneratorActiviti;

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

	public String taskFormGenerator(String activitiId) throws ExceptionGeneratorActiviti{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		for(Task task : taskService.createTaskQuery().list()){
			FormPropertyReader.generateFormProperty(task, processEngine.getFormService().getTaskFormData(task.getId()));
		}
		
		return "";
	}

}
