package org.linagora.communicate;

import org.linagora.dao.openpaas.TypeRequest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class OpenPaasOrchestrator {

	private final String type = "application/json";

	public String wsCalendar(ApacheHttpClient client, String webServicePath, String json, TypeRequest request) {
		try {
			if (client != null) {
				WebResource webResource = client.resource(webServicePath);
				ClientResponse response = null;

				if (request.equals(TypeRequest.POST))
					response = webResource.type(type).post(ClientResponse.class, json);
				else if (request.equals(TypeRequest.PUT))
					response = webResource.type(type).put(ClientResponse.class, json);

				if (response == null || response.getStatus() >= 210) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}
				return response.getEntity(String.class);
			}
			throw new RuntimeException("Failed : HTTP Login");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
