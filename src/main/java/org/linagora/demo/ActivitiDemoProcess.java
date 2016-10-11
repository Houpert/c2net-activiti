package org.linagora.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.linagora.dao.ActivitiDAO;


public class ActivitiDemoProcess {

	private Logger Log = Logger.getLogger(ActivitiDemoProcess.class);


	public void demoProcess(ActivitiDAO bpmn) throws IOException{
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();

		InputStream is = new FileInputStream(bpmn.getFile());
		
		repositoryService.createDeployment().addInputStream(bpmn.getName(), is).deploy();
		//repositoryService.createDeployment().addClasspathResource(file.getName()).deploy();
		//TODO REFRESH SOMETHING HERE
		Log.info("##_Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");

		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.startProcessInstanceByKey(bpmn.getProcessId(), variables);

		// Verify that we started a new process instance
		Log.info("##_Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
		TaskService taskServicePrint = processEngine.getTaskService();

		for(Task task : taskServicePrint.createTaskQuery().list()){
			Log.info("Asi : "+task.getAssignee());
			Log.info("Cat : "+task.getCategory());
			Log.info("Des : "+task.getDescription());
			Log.info("Exe : "+task.getExecutionId());
			Log.info("For : "+task.getFormKey());
			Log.info("Id  : "+task.getId());
			Log.info("Nam : "+task.getName());
			Log.info("Own : "+task.getOwner());
			Log.info("Dat : "+task.getDueDate());
			Log.info("CrT : "+task.getCreateTime());
			Log.info("TLV : "+task.getTaskLocalVariables());
		}


		// Fetch all tasks for the management group
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
			Log.info("1.##_Task available: " + task.getName());
			Log.info("1.##_Task available: " + task.getDescription());

		}

		Task currentTask = tasks.get(0);

		Map<String, Object> taskVariablesFalse = new HashMap<String, Object>();
		taskVariablesFalse.put("vacationApproved", "true");
		taskVariablesFalse.put("managerMotivation", "OK !");
		taskService.complete(currentTask.getId(), taskVariablesFalse);

		taskService = processEngine.getTaskService();
		tasks = taskService.createTaskQuery().taskAssignee("Kermit").list();
		for (Task task : tasks) {
			Log.info("##2._Task available: " + task.getName());
		}

		sendOpenPaasNotification();
	}


	private void sendOpenPaasNotification() throws IOException {
		// TODO Auto-generated method stub

		String jsonNotification = "{"
				+ "\"title\": \"My notification\","
				+ "\"action\": \"create\","
				+ "\"object\": \"form\","
				+ "\"link\": \"http://localhost:8888\","
				+ " \"author\": \"53a946c41f6d7a5d729e0477\","
				//+ " target: [ {objectType: 'user', id: 5757e32933e422073571f0aa}, {objectType: 'user', id: 53a946c41f6d7a5d729e0479} ],"
				+ " \"target\": [ {\"objectType\": \"user\", \"id\": 5757e32933e422073571f0aa},  {\"objectType\": \"user\", \"id\": 53a946c41f6d7a5d729e0479} ],"
				+ "\"read\": false,"
				+ "\"timestamps\": { \"creation\": \"Tue Jun 24 2014 11:37:08 GMT+0200 (CEST)\" },"
				+ " \"level\": \"info\""
				+ "}";

		
		JSONObject jsonObject = new JSONObject(jsonNotification);
		System.out.println(jsonObject);

		try {
			URL url = new URL("http://localhost:8082/api/notifications");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(jsonObject.toString());
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while (in.readLine() != null) {
			}
			System.out.println("\nCrunchify REST Service Invoked Successfully..");
			in.close();
		} catch (Exception e) {
			System.out.println("\nError while calling Crunchify REST Service");
			System.out.println(e);
		}

	}

}
