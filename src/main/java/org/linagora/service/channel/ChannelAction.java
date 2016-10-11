package org.linagora.service.channel;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.ServiceAction;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChannelAction implements ServiceAction{

	@Override
	public String getXML(MultipartFile file)  throws ExceptionGeneratorActiviti{
		// TODO Auto-generated method stub
		return null;
	}
}