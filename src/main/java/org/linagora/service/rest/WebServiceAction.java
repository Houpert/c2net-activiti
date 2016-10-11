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
	public String getXML(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{
		ActivitiDAO myActivitiFile;
		try {
			ActivitiParse myActivitiGenerator = new ActivitiParse();

			myActivitiFile = myActivitiGenerator.parseXMLToActiviti(file);

			if(myActivitiFile == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti XML");
		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
		
		try{
			ActivitiProcess ap = new ActivitiProcess();
			ap.execution(myActivitiFile);
			
		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
		
		return myActivitiFile.getName();
	}

	@RequestMapping("/start")
	public String startProcess(@RequestParam(value="id") String id) {
		System.out.println("StartMyId");

		return id;
	}
}