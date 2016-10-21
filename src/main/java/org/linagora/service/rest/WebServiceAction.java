package org.linagora.service.rest;

import java.util.Map;

import org.linagora.activiti.ActivitiProcess;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.ServiceAction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/action")
public class WebServiceAction implements ServiceAction{

	/**
	 * This web service parse an XML to be readable for Activiti
	 *
	 * @param String xml	The XML of BPMN to execute with Activiti
	 *
	 * @throws DSSException
	 */

	@RequestMapping(value = "/parse", method = RequestMethod.POST)
	public String generateBpmn(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{
		return ActivitiProcess.initBpmnIoToActiviti(file);
	}

	@RequestMapping(value = "/task/list", method = RequestMethod.GET)
	public String listTask() throws ExceptionGeneratorActiviti {
		return ActivitiProcess.listTaskForm();
	}

	@RequestMapping(value = "/task/complet", method = RequestMethod.POST)
	public boolean completeTask(@RequestBody Map<String, Object>  map) throws ExceptionGeneratorActiviti {
		return ActivitiProcess.completeTask(map);
	}
}