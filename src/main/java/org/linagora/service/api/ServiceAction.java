package org.linagora.service.api;

import java.util.Map;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.springframework.web.multipart.MultipartFile;

public interface ServiceAction {

	public String generateBpmn(MultipartFile file) throws ExceptionGeneratorActiviti;
	
	public String listTask() throws ExceptionGeneratorActiviti;

	public boolean completeTask(Map<String, Object>  json) throws ExceptionGeneratorActiviti;

}
