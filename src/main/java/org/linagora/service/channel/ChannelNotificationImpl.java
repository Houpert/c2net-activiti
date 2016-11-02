package org.linagora.service.channel;

import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.api.ServiceAction;
import org.linagora.service.api.ServiceNotification;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChannelNotificationImpl implements ServiceNotification{

	@Override
	public String sendNotification(String id) throws ExceptionGeneratorActiviti {
		// TODO Auto-generated method stub
		return null;
	}

}