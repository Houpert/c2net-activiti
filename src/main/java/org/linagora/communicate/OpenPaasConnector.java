package org.linagora.communicate;

import java.net.SocketException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.linagora.dao.ActionActiviti;
import org.linagora.dao.OpenPaasUser;
import org.linagora.dao.openpaas.TypeRequest;
import org.linagora.dao.openpaas.notification.NotificationOP;
import org.linagora.utility.PropertyFile;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

import biweekly.Biweekly;
import biweekly.ICalendar;
import net.fortuna.ical4j.model.Calendar;

public class OpenPaasConnector {
	private final String configFilePath = "config/config.properties";

	private String webServiceApi;
	private String loginApiPath;
	private String notificationApiPath;
	private String calendarsApiPath;

	private String userId;

	private final String type = "application/json";
	private final OpenPaasOrchestrator opc = new OpenPaasOrchestrator();

	private WebResource webResource;
	private ApacheHttpClient client;

	private NotificationUtility notificationUtility;
	private CalendarUtility calendarUtility;

	public OpenPaasConnector() {
		try {
			init();

			notificationUtility = new NotificationUtility();
			calendarUtility = new CalendarUtility();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getWebServiceApi() {
		return webServiceApi;
	}

	public String getPath(ActionActiviti action) {
		switch (action) {
		case MAIL:
			return webServiceApi; // TODO
		case CALENDAR:
			return webServiceApi + calendarsApiPath
					+ "/5757e32933e422073571f0aa/events/1ed80fa7-6738-49f1-95c7-3fad9e03c869.ics?graceperiod=10000"; // TODO
		case NOTIFICATION:
			return webServiceApi + notificationApiPath;
		default:
			return null;
		}
	}

	public String getLoginApiPath() {
		return webServiceApi + loginApiPath;
	}

	public static void main(String[] args) throws SocketException, ParseException, URISyntaxException {
		OpenPaasUser user = new OpenPaasUser("admin@open-paas.org", "secret");
		OpenPaasConnector op = new OpenPaasConnector();
		List<String> attendeeList = new ArrayList<String>();
		attendeeList.add("user0@open-paas.org");
		attendeeList.add("user10@open-paas.org");
		System.out.println("MyTestWithLogin");

		Calendar cal = op.getCalendarUtility().createCalendar("MyDoubleEvent", attendeeList, "admin@open-paas.org", "Somewhere");

		OpenPaasConnector opc = new OpenPaasConnector();

		opc.wsCall(user, ActionActiviti.CALENDAR, cal);
	}

	private void init() throws Exception {
		try {
			PropertyFile propertyFile = new PropertyFile();
			Properties prop = propertyFile.getProperties(configFilePath);

			webServiceApi = prop.getProperty("service.webservice");
			loginApiPath = prop.getProperty("service.login");
			notificationApiPath = prop.getProperty("service.notification");
			calendarsApiPath = prop.getProperty("service.calendars");

		} catch (Exception e) {
			throw e;
		}
	}

	private void prepareRequest(OpenPaasUser user, String ws) {
		HTTPBasicAuthFilter auth = new HTTPBasicAuthFilter(user.getUsername(), user.getPassword());
		ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
		config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
		client = ApacheHttpClient.create(config);
		client.addFilter(auth);

		webResource = client.resource(webServiceApi + ws);

	}

	private String readJsonId(String json) {
		JSONObject jsnobject = new JSONObject(json);
		return jsnobject.getString("_id");
	}

	public ApacheHttpClient login(OpenPaasUser user) {
		try {
			prepareRequest(user, loginApiPath);
			ClientResponse response = webResource.type(type).post(ClientResponse.class, user.generateJson());

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String result = response.getEntity(String.class);
			userId = readJsonId(result);
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void wsCall(OpenPaasUser user, ActionActiviti action, Object request) {
		ApacheHttpClient client = login(user);
		String json = null;
		TypeRequest type = TypeRequest.POST;
		
		switch (action) {
		case MAIL:
			// Something..
			break;
		case CALENDAR:
			Calendar cal = (Calendar) request;
			json = Biweekly.parse(cal.toString()).first().writeJson();
			type = TypeRequest.PUT;
			break;
		case NOTIFICATION:
			NotificationOP notification = (NotificationOP) request;
			notification.setAuthor(userId);
			json = notification.generateJson();
			break;
		}

		String response = opc.wsCalendar(client,
				"http://localhost:8080/dav/api/calendars/5757e32933e422073571f0aa/events/70b86190-7530-40dc-8eee-9e93deecefa9.ics?graceperiod=1",
				json, type);
	}

	public String generateJson(Object obj) {
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		json = json.toLowerCase();
		return json;
	}

	public String getUserId() {
		return userId;
	}

	public NotificationUtility getNotificationUtility() {
		return notificationUtility;
	}

	public CalendarUtility getCalendarUtility() {
		return calendarUtility;
	}

}
