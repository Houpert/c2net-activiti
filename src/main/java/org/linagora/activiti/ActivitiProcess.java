package org.linagora.activiti;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ManualTask;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.form.FormData;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.linagora.activiti.form.Form;
import org.linagora.activiti.form.Formly;
import org.linagora.dao.ActivitiDAO;
import org.linagora.dao.ProcessData;
import org.linagora.dao.TaskActiviti;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiParse;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

public class ActivitiProcess {

	private final String MY_TASK_KEY = "taskId";

	public void optimisation() {
	}

	public String initBpmnIoToActiviti(MultipartFile file, boolean execute) throws ExceptionGeneratorActiviti {
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
			if (execute) {
				String idNumber = saveAndExecute(myActivitiFile);
				myActivitiFile.setIdNumber(idNumber);
			} else {
				saveBpmn(myActivitiFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to execute the bpmn " + e.getMessage());
		}
		return myActivitiFile.generateJson();
	}

	public String saveBpmn(ActivitiDAO bpmn) throws FileNotFoundException {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		InputStream inputStream = new FileInputStream(bpmn.getFile());

		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().name(bpmn.getProcessId()).addInputStream(bpmn.getName(), inputStream)
				.deploy();
		return bpmn.getProcessId();
	}

	public String executeBpmn(String id) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(id);

		return pi.getId();
	}

	public String saveAndExecute(ActivitiDAO bpmn) throws FileNotFoundException {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		InputStream inputStream = new FileInputStream(bpmn.getFile());

		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().name(bpmn.getProcessId()).addInputStream(bpmn.getName(), inputStream)
				.deploy();

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

	public boolean completeUserTask(Map<String, Object> mapAttribute) throws ExceptionGeneratorActiviti {
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

	public boolean completeReiceiveTask(String processId, String receiveTaskid) throws ExceptionGeneratorActiviti {
		try {
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(processId)
					.activityId(receiveTaskid).singleResult();

			if (execution == null) {
				throw new ActivitiObjectNotFoundException("Unable to find the receiveTask");
			}

			runtimeService.signal(execution.getId());
		} catch (ActivitiObjectNotFoundException e) {
			throw new ExceptionGeneratorActiviti("Unable to find the ProcessId or ReceiveTaskId");
		}

		return true;
	}

	public String dataReader() throws ExceptionGeneratorActiviti {
		List<ProcessData> dataPrint = new ArrayList<ProcessData>();

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine.getRepositoryService();

		List<ProcessInstance> dataList = runtimeService.createProcessInstanceQuery().list();

		for (ProcessInstance data : dataList) {
			TaskActiviti taskType;

			Execution execution = runtimeService.createExecutionQuery().processInstanceId(data.getId())
					.activityId(data.getActivityId()).singleResult();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(execution.getProcessInstanceId()).singleResult();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
			FlowElement flowElement = bpmnModel.getFlowElement(((DelegateExecution) execution).getCurrentActivityId());
			if (flowElement instanceof ReceiveTask)
				taskType = TaskActiviti.RECEIVE_TASK;
			else if (flowElement instanceof UserTask)
				taskType = TaskActiviti.USER_TASK;
			else if (flowElement instanceof ManualTask)
				taskType = TaskActiviti.MANUAL_TASK;
			else if (flowElement instanceof ServiceTask)
				taskType = TaskActiviti.SERVICE_TASK;
			else
				taskType = TaskActiviti.TASK;

			dataPrint.add(
					new ProcessData(data.getId(), data.getProcessDefinitionName(), taskType, data.getActivityId()));
		}
		return new Gson().toJson(dataPrint);
	}

	public String listTaskFormMail(String email) throws ExceptionGeneratorActiviti {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();

		List<Form> listForm = new ArrayList<Form>();
		for (Task task : taskService.createTaskQuery().list()) {
			if (task.getAssignee() == null || task.getAssignee().equals(email)) {
				FormData taskForm = processEngine.getFormService().getTaskFormData(task.getId());
				List<Formly> formly = ActivitiFormGenerator.generateForm(task, taskForm);
				listForm.add(new Form(task.getId(), formly));
			}
		}
		Gson gson = new Gson();
		return gson.toJson(listForm);
	}
}
