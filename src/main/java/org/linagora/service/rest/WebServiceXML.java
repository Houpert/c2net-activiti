package org.linagora.service.rest;

import org.linagora.dao.ActivitiBpmn;
import org.linagora.dao.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiGenerator;
import org.linagora.service.ServiceXML;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class WebServiceXML implements ServiceXML{

	/**
	 * This web service parse an XML to be readable for Activiti
	 *
	 * @param String xml	The XML of BPMN to execute with Activiti
	 *
	 * @throws DSSException
	 */

	@RequestMapping("/generate/bpmn")
	public String getXML(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{

		try {
			ActivitiGenerator myActivitiGenerator = new ActivitiGenerator();

			ActivitiBpmn activiti = myActivitiGenerator.parseXMLToActiviti(file);
			
			if(activiti == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti XML");
			
			//if(myActivitiGenerator.saveBPMNFile(activiti))
				return activiti.getName();
			/*else 
				throw new ExceptionGeneratorActiviti("Error during the file creation");*/

		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
	}
	
	@RequestMapping("/test/parse")
	public String parseDefault() throws ExceptionGeneratorActiviti{

	return null;
	}
}