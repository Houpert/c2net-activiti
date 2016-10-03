package org.linagora.service.rest;

import java.io.File;

import org.linagora.dao.exception.ExceptionGeneratorActiviti;
import org.linagora.parse.ActivitiParse;
import org.linagora.service.ServiceXML;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/action")
public class WebServiceAction implements ServiceXML{

	/**
	 * This web service parse an XML to be readable for Activiti
	 *
	 * @param String xml	The XML of BPMN to execute with Activiti
	 *
	 * @throws DSSException
	 */

	@RequestMapping("/parse")
	public String getXML(@RequestParam("file") MultipartFile file) throws ExceptionGeneratorActiviti{

		try {
			ActivitiParse myActivitiGenerator = new ActivitiParse();

			File myActivitiFile = myActivitiGenerator.parseXMLToActiviti(file);

			if(myActivitiFile == null)
				throw new ExceptionGeneratorActiviti("Unable to parse the xml to an activiti XML");

			return myActivitiFile.getName();

		} catch(Exception e){
			e.printStackTrace();
			throw new ExceptionGeneratorActiviti("Unable to parse the XML "+e.getMessage());
		}
	}

	@RequestMapping("/start")
	public String startProcess(@RequestParam(value="id") String id) {

		return id;
	}
}