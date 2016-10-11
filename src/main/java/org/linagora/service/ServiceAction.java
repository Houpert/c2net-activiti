package org.linagora.service;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.springframework.web.multipart.MultipartFile;

public interface ServiceAction {

	public String getXML(MultipartFile file) throws ExceptionGeneratorActiviti;
	
}
