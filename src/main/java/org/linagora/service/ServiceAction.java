package org.linagora.service;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.springframework.web.multipart.MultipartFile;

public interface ServiceAction {

	public String generateBpmn(MultipartFile file) throws ExceptionGeneratorActiviti;
	
	public String checkTask(String activitiId) throws ExceptionGeneratorActiviti;

	public String completeTask(String nameTask, String idTask) throws ExceptionGeneratorActiviti;

}
