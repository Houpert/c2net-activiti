package org.linagora.activiti.service;

import java.util.ArrayList;
import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.linagora.utility.ServiceUtility;
import org.springframework.stereotype.Component;

@Component("startProcessInstanceDelegate")
public class NotificationTask implements JavaDelegate {

	private Expression email, message, title;
	private String COMPONENT = "OPP";
	private String LINK = "/#/";
	private String ICON = "''";

	public void execute(DelegateExecution execution) throws Exception {
		String emailStr, messageStr, titleStr;
		ArrayList<String> list = new ArrayList<String>();
		list.add(COMPONENT);
		list.add(LINK);
		list.add(ICON); // TODO gonna be remove by Wael, probably

		try {

			emailStr = (String) email.getValue(execution);
			messageStr = (String) message.getValue(execution);
			titleStr = (String) title.getValue(execution);

			list.add(emailStr);
			list.add(messageStr);
			list.add(titleStr);
			list.add(ServiceUtility.getVariableMessage(execution));

		} catch (Exception e) {
			throw e;
		}

		callDKMSNotificationFactory(list);
	}

	private void callDKMSNotificationFactory(ArrayList<String> listNotification) {
		// TODO Manage DKMS notification call
	}
}