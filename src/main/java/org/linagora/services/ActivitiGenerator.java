package org.linagora.services;

import org.linagora.dao.ActivitiBpmn;

public class ActivitiGenerator {
	
	public ActivitiBpmn parseXMLToActiviti(String xml){
		String bpmnXML = xml;
		String name = "FakeName";
		//TODO Parse XML for activiti reader
		
		return new ActivitiBpmn(name, bpmnXML);
	}

	public boolean createBPMNFile(ActivitiBpmn activitiBpmn) {
		boolean isCreate = false;
		
		// TODO Create BPMN File with XML Parser
		
		return isCreate;
	}
	
}
