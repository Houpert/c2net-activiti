package org.linagora.activiti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.linagora.dao.ActivitiDAO;

public class ActivitiProcess{

	public void optimisation(){
		
	}
	
	public void execution(ActivitiDAO bpmn) throws FileNotFoundException{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		InputStream is = new FileInputStream(bpmn.getFile());
		Deployment dp = repositoryService.createDeployment().addInputStream(bpmn.getName(), is).deploy();
	}
	
}
