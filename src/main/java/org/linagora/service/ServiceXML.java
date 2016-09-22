package org.linagora.service;

import org.linagora.dao.exception.ExceptionGeneratorActiviti;
import org.springframework.web.multipart.MultipartFile;

public interface ServiceXML {

	public String getXML(MultipartFile file) throws ExceptionGeneratorActiviti;
	
}
