package org.linagora.communicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.linagora.dao.openpaas.ActionNotification;
import org.linagora.dao.openpaas.LevelNotification;
import org.linagora.dao.openpaas.ObjectNotification;
import org.linagora.dao.openpaas.ObjectType;
import org.linagora.dao.openpaas.OpenPaasNotification;
import org.linagora.dao.openpaas.OpenPaasUser;
import org.linagora.dao.openpaas.TargetNotification;
import org.linagora.utility.PropertyFile;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class NotificationUtility {

	private final static String configFilePath = "config/config.properties";

	private static String webServiceApi;
	private static String loginApiPath;
	private static String notificationApiPath;

	private final static String type = "application/json";

	private static WebResource webResource;
	private static ApacheHttpClient client;

	public static void main(String[] args) {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		OpenPaasUser user = new OpenPaasUser("admin@open-paas.org", "secret");
		OpenPaasNotification notification = createNotification("http://localhost:3000/form/myNewNotification",
				"5757e32933e422073571f0aa", "5757e32933e422073571f0aa");
		notification(user, notification);

	}

	private static void init() throws Exception {
		try {
			PropertyFile propertyFile = new PropertyFile();
			Properties prop = propertyFile.getProperties(configFilePath);

			webServiceApi = prop.getProperty("service.webservice");
			loginApiPath = prop.getProperty("service.login");
			notificationApiPath = prop.getProperty("service.notification");
		} catch (Exception e) {
			throw e;
		}
	}

	private static OpenPaasNotification createNotification(String link, String author, String target) {
		List<TargetNotification> targetNotification = new ArrayList<TargetNotification>();
		targetNotification.add(new TargetNotification(ObjectType.USER, target));

		OpenPaasNotification opNot = new OpenPaasNotification("MyActivitiNotification", ActionNotification.CREATED,
				ObjectNotification.FORM, link, LevelNotification.TRANSIENT, author, targetNotification);

		return opNot;
	}

	private static String readJsonId(String json) {
		JSONObject jsnobject = new JSONObject(json);
		return jsnobject.getString("_id");
	}

	private static void prepareRequest(OpenPaasUser user, String ws) {
		HTTPBasicAuthFilter auth = new HTTPBasicAuthFilter(user.getUsername(), user.getPassword());
		ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
		config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
		client = ApacheHttpClient.create(config);
		client.addFilter(auth);

		webResource = client.resource(webServiceApi + ws);

	}

	private static String login(OpenPaasUser user) {
		try {
			prepareRequest(user, loginApiPath);
			ClientResponse response = webResource.type(type).post(ClientResponse.class, user.generateJson());

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String result = response.getEntity(String.class);
			readJsonId(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static String notification(OpenPaasUser user, OpenPaasNotification opNotification) {
		try {
			String idUser = login(user);
			if (idUser != null) {
				webResource = client.resource(webServiceApi + notificationApiPath);
				opNotification.setAuthor(idUser);
				ClientResponse response = webResource.type(type).post(ClientResponse.class,
						opNotification.generateJson());

				if (response.getStatus() != 201) {
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