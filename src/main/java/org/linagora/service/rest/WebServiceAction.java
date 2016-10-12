package org.linagora.service.rest;

import org.linagora.activiti.ActivitiProcess;
import org.linagora.dao.ActivitiDAO;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiParse;
import org.linagora.service.ServiceAction;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@RequestMapping("/parse")
	public String generateBpmn(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{
		ActivitiDAO myActivitiFile;
		try {
			ActivitiParse myActivitiGenerator = new ActivitiParse();

			myActivitiFile = myActivitiGenerator.parseXMLToActiviti(file);

			if(myActivitiFile == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti executable");
		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
		
		try{
			ActivitiProcess ap = new ActivitiProcess();
			String activitiId = ap.execution(myActivitiFile);
			
		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to execute the bpmn"+e.getMessage());
		}
		
		return myActivitiFile.getName();
	}

	@RequestMapping("/task/list")
	public String checkTask(@RequestParam("id") String activitiId) throws ExceptionGeneratorActiviti {
		ActivitiProcess ap = new ActivitiProcess();
		String result = ap.taskFormGenerator(activitiId);
		
		return result;
	}

	@RequestMapping("/task/complete")

	public String completeTask(String nameTask, String idTask) throws ExceptionGeneratorActiviti {
		// TODO Auto-generated method stub
		return null;
	}
}