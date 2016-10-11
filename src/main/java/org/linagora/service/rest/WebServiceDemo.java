package org.linagora.service.rest;

import org.linagora.dao.ActivitiDAO;
import org.linagora.demo.ActivitiDemoProcess;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiParse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/action")
public class WebServiceDemo {

	@RequestMapping("/parse/test")
	public String testWebServiceDemo(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{
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
		
		try {
			ActivitiDemoProcess adp = new ActivitiDemoProcess();

			adp.demoProcess(myActivitiFile);

			System.out.println(myActivitiFile.getName());
			return myActivitiFile.getName();
		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Error during execution bpmn "+e.getMessage());
		}
	}
	
}
