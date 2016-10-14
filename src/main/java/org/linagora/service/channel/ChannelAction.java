package org.linagora.service.channel;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.ServiceAction;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChannelAction implements ServiceAction{

	@Override
	public String generateBpmn(MultipartFile file)  throws ExceptionGeneratorActiviti{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listTask() throws ExceptionGeneratorActiviti {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String completeTask(String json) throws ExceptionGeneratorActiviti {
		// TODO Auto-generated method stub
		return null;
	}
}