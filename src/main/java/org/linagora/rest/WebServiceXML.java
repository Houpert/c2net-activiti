package org.linagora.rest;

import org.linagora.dao.ActivitiBpmn;
import org.linagora.rest.exception.ExceptionGeneratorActiviti;
import org.linagora.services.ActivitiGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceXML {

	/**
	 * This web service parse an XML to be readable for Activiti
	 *
	 * @param String xml	The XML of BPMN to execute with Activiti
	 *
	 * @throws DSSException
	 */

	@RequestMapping("/generate/bpmn")
	public String getXML(@RequestParam(value="xml") String bpmnXML) throws ExceptionGeneratorActiviti{

		try {
			ActivitiGenerator myActivitiGenerator = new ActivitiGenerator();

			ActivitiBpmn activiti = myActivitiGenerator.parseXMLToActiviti(bpmnXML);
			
			if(activiti == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti XML");

			if(myActivitiGenerator.createBPMNFile(activiti))
				return activiti.getName();
			else 
				throw new ExceptionGeneratorActiviti("Error during the file creation");

		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
	}
}