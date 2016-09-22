package org.linagora.service.channel;

import org.linagora.dao.exception.ExceptionGeneratorActiviti;
import org.linagora.service.ServiceXML;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class WebServiceXML implements ServiceXML{

	@Override
	public String getXML(MultipartFile file)  throws ExceptionGeneratorActiviti{
		// TODO Auto-generated method stub
		return null;
	}
}