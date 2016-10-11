package org.linagora.service;

import org.linagora.exception.ExceptionGeneratorActiviti;

public interface ServiceNotification {

	public String sendNotification(String id) throws ExceptionGeneratorActiviti;

}
