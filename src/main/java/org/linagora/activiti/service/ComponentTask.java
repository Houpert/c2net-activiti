package org.linagora.activiti.service;

import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.linagora.utility.PropertyFile;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component("startProcessInstanceDelegate")
public class ComponentTask implements JavaDelegate {

	private Expression componentName;
	private Expression json;

	private static String componentService = null;

	public void execute(DelegateExecution execution) throws Exception {
		getComponentService();
		boolean isGet = false;
		String jsonStr = null;
		String componentNameStr = null;

		try {
			componentNameStr = (String) componentName.getValue(execution);
		} catch (Exception e) {
			throw e;
		}

		try {
			jsonStr = (String) json.getValue(execution);
		} catch (Exception e) {
			isGet = true;
		}

		Client client = Client.create();
		WebResource webResource = client.resource(componentService + componentNameStr);

		if (isGet) {
			webResource.get(String.class);
		} else {
			webResource.accept("application/json").type("application/json").post(ClientResponse.class, jsonStr);
		}
	}

	public String getComponentService() throws Exception {
		if (componentService == null) {
			PropertyFile propManager = new PropertyFile();
			Properties prop = propManager.getProperties("config/config.properties");
			componentService = prop.getProperty("chanel.service");
		}
		return componentService;
	}
}