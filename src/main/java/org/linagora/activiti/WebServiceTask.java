package org.linagora.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.util.json.JSONObject;
import org.linagora.dao.VariableData;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component("startProcessInstanceDelegate")
public class WebServiceTask implements JavaDelegate {

	private Expression url;
	private Expression json;

	public void execute(DelegateExecution execution) throws Exception {
		String jsonStr = null;
		String urlStr = null;
		boolean isGet = false;

		try {
			urlStr = (String) url.getValue(execution);
		} catch (Exception e) {
			throw e;
		}

		try {
			jsonStr = (String) json.getValue(execution);
		} catch (Exception e) {
			// No JSON -> GET
			isGet = true;
		}

		Client client = Client.create();
		WebResource webResource = client.resource(urlStr);

		if (isGet) {
			String result = webResource.get(String.class);
			try {
				JSONObject jsonObj = new JSONObject(result);
				VariableData vd = new VariableData(jsonObj.get("name").toString(), jsonObj.get("value").toString());
				try {
					double d = Double.parseDouble(vd.getValue());
					System.out.println(d);
					execution.setVariableLocal(vd.getName(), d);
				} catch (NumberFormatException e) {
					execution.setVariableLocal(vd.getName(), vd.getValue());
				}

			} catch (Exception e) {
				// new Exception("The result of the get is not a valide data",
				// e.getCause()).printStackTrace();
			}

		} else {
			webResource.accept("application/json").type("application/json").post(ClientResponse.class, jsonStr);
		}
	}

}