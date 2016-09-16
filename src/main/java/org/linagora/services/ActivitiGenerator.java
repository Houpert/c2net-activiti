package org.linagora.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.linagora.dao.ActivitiBpmn;

public class ActivitiGenerator {

	public ActivitiBpmn parseXMLToActiviti(String xml){
		String bpmnXML = xml;
		String name = "FakeName";
		//TODO Parse XML for activiti reader

		return new ActivitiBpmn(name, bpmnXML);
	}

	public boolean saveBPMNFile(ActivitiBpmn activitiBpmn) {
		// TODO Create BPMN File with 
		Database database = new Database();
		String name = database.create(activitiBpmn);

		if(name == null)
			return false;
		
		return true;
	}

}
