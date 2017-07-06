package org.linagora.service.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.linagora.activiti.ActivitiProcess;
import org.linagora.dao.cnet.ReceiveTaskData;
import org.linagora.dao.cnet.UserEmail;
import org.linagora.dao.cnet.VariableActiviti;
import org.linagora.dao.cnet.WorkflowData;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.api.ServiceAction;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/action")
public class ServiceActionImpl implements ServiceAction {

	/**
	 * This web service parse an XML to be readable for Activiti
	 *
	 * @param String
	 *            xml The XML of BPMN to execute with Activiti
	 *
	 * @throws ExceptionGeneratorActiviti
	 */

	/* Save and Execution */
	private ActivitiProcess activiti = new ActivitiProcess();

	@RequestMapping(value = "/parse/save", method = RequestMethod.POST)
	public String saveBpmn(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti {
		return activiti.initBpmnIoToActiviti(file, false);
	}

	@RequestMapping(value = "/parse/execute", method = RequestMethod.POST)
	public String generateBpmn(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti {
		return activiti.initBpmnIoToActiviti(file, true);
	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public String executeBpmn(@RequestParam("nameProcess") String nameProcess) throws ExceptionGeneratorActiviti {
		return activiti.executeBpmn(nameProcess);
	}

	/*C2NET Context*/
	@RequestMapping(value = "/parse/orchestrate", method = RequestMethod.POST)
	public String generateBpmn(@RequestBody WorkflowData workflowData) throws ExceptionGeneratorActiviti, IOException {
		String xmlstring = workflowData.getXmlstring();
		String filename = workflowData.getFilename();

		File temp = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		bw.write(xmlstring);
		bw.close();

		MultipartFile file = getMockCommonsMultipartFile(temp);
		return activiti.initBpmnIoToActiviti(file, true);
	}

	private MultipartFile getMockCommonsMultipartFile(File file) throws IOException {
		FileInputStream inputFile = new FileInputStream(file.getAbsolutePath());  
		MockMultipartFile multipartFile = new MockMultipartFile("bpmn_"+file.getName(), file.getName(), "multipart/form-data", inputFile);
		return multipartFile;
	}

	/* Manage task side*/
	@RequestMapping(value = "/task/list", method = RequestMethod.POST)
	public String listTask(@RequestParam("email") String email) throws ExceptionGeneratorActiviti {
		return activiti.listTask(email);
	}

	/*C2NET Context*/
	@RequestMapping(value = "/task/list/mail", method = RequestMethod.POST)
	public String listTask(@RequestBody UserEmail emailData) throws ExceptionGeneratorActiviti {
		String email = emailData.getEmail();
		return activiti.listTask(email);
	}

	@RequestMapping(value = "/task/complet", method = RequestMethod.POST)
	public boolean completeTask(@RequestBody Map<String, Object> map) throws ExceptionGeneratorActiviti {
		return activiti.completeUserTask(map);
	}

	@RequestMapping(value = "/task/receive", method = RequestMethod.POST)
	public boolean executeReceiveTask(@RequestParam("processId") String processId,
			@RequestParam("taskId") String receiveTaskId) throws ExceptionGeneratorActiviti {
		return activiti.completeReiceiveTask(processId, receiveTaskId, null);
	}

	@RequestMapping(value = "/task/receive/json", method = RequestMethod.POST)
	public boolean executeReceiveTask(@RequestParam("processId") String processId,
			@RequestParam("taskId") String receiveTaskId, @RequestParam("json") String json) throws ExceptionGeneratorActiviti {
		return activiti.completeReiceiveTask(processId, receiveTaskId, json);
	}

	@RequestMapping(value = "/task/dkms", method = RequestMethod.POST)
	public boolean executeAllReceiveTask() throws ExceptionGeneratorActiviti {
		return activiti.completeAllReiceiveTask(null);
	}
	@RequestMapping(value = "/task/dkms/gson", method = RequestMethod.POST)
	public boolean executeAllReceiveTask(@RequestBody VariableActiviti vData) throws ExceptionGeneratorActiviti {
		String json="{\"name\":\""+vData.getName()+"\", \"value\":\""+vData.getValue()+"\"}";

		return activiti.completeAllReiceiveTask(json);
	}

	/*C2net Process*/
	@RequestMapping(value = "/task/receive/gson", method = RequestMethod.POST)
	public boolean executeReceiveTask(@RequestBody ReceiveTaskData rData) throws ExceptionGeneratorActiviti {
		String processId=rData.getProcessId();
		String receiveTaskId = rData.getTaskId();
		String json="{\"name\":\""+rData.getName()+"\", \"value\":\""+rData.getValue()+"\"}";
		return activiti.completeReiceiveTask(processId, receiveTaskId, json);
	}

	/*All data*/
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String allData() throws ExceptionGeneratorActiviti {
		return activiti.dataReader();
	}
}