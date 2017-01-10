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
import org.activiti.engine.form.FormData;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.Form;
import org.linagora.activiti.form.Formly;
import org.linagora.dao.ActivitiDAO;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiParse;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

public class ActivitiProcess {

	private final String MY_TASK_KEY = "taskId";

	public void optimisation() {
	}

	public String initBpmnIoToActiviti(MultipartFile file) throws ExceptionGeneratorActiviti {
		ActivitiDAO myActivitiFile = null;
		try {
			ActivitiParse myActivitiGenerator = new ActivitiParse();
			myActivitiFile = myActivitiGenerator.parseXMLToActivitiExecutable(file);

			if (myActivitiFile == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti executable");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML " + e.getMessage());
		}

		try {
			String idNumber = execution(myActivitiFile);
			myActivitiFile.setIdNumber(idNumber);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to execute the bpmn " + e.getMessage());
		}

		return myActivitiFile.generateJson();
	}

	public String execution(ActivitiDAO bpmn) throws FileNotFoundException {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		InputStream inputStream = new FileInputStream(bpmn.getFile());

		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addInputStream(bpmn.getName(), inputStream).deploy();

		// Start the processus
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(bpmn.getProcessId());
		return pi.getId();
	}

	public String listTaskForm() throws ExceptionGeneratorActiviti {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		List<Form> listForm = new ArrayList<Form>();
		for (Task task : taskService.createTaskQuery().list()) {
			FormData taskForm = processEngine.getFormService().getTaskFormData(task.getId());
			List<Formly> formly = ActivitiFormGenerator.generateForm(task, taskForm);
			listForm.add(new Form(task.getId(), formly));
		}
		Gson gson = new Gson();
		return gson.toJson(listForm);
	}

	public boolean completeTask(Map<String, Object> mapAttribute) throws ExceptionGeneratorActiviti {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		String taskId = (String) mapAttribute.get(MY_TASK_KEY);
		mapAttribute.remove(MY_TASK_KEY);

		try {
			taskService.complete(taskId, mapAttribute);
		} catch (ActivitiObjectNotFoundException e) {
			throw new ExceptionGeneratorActiviti("Unable to find the task id");
		}

		return true;
	}

	public boolean completeReiceive(String processId, String receiveTaskid) throws ExceptionGeneratorActiviti {
		try {
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(processId)
					.activityId(receiveTaskid).singleResult();

			if (execution == null) {
				throw new ActivitiObjectNotFoundException("ProcessId or ReceiveTaskId not found");
			}

			runtimeService.signal(execution.getId());
		} catch (ActivitiObjectNotFoundException e) {
			throw new ExceptionGeneratorActiviti("Unable to find the task id");
		}

		return true;

	}
}
