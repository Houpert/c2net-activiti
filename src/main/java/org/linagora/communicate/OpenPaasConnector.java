package org.linagora.communicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.linagora.dao.ActionActiviti;
import org.linagora.dao.OpenPaasUser;
import org.linagora.dao.openpaas.TypeRequest;
import org.linagora.dao.openpaas.notification.NotificationOP;
import org.linagora.utility.PropertyFile;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

import biweekly.Biweekly;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

public class OpenPaasConnector {
	private final String configFilePath = "config/config.properties";

	private String webServiceApi;
	private String loginApiPath;
	private String notificationApiPath;
	private String calendarsApiPath;

	private String userId;
	private String uiId;

	private final String type = "application/json";
	private final OpenPaasOrchestrator opc = new OpenPaasOrchestrator();
	private OpenPaasUser opu;

	private WebResource webResource;
	private ApacheHttpClient client;

	private NotificationUtility notificationUtility;
	private CalendarUtility calendarUtility;

	public OpenPaasConnector(){
		try {
			PropertyFile propertyFile = new PropertyFile();
			Properties prop = propertyFile.getProperties(configFilePath);

			webServiceApi = prop.getProperty("service.webservice");
			loginApiPath = prop.getProperty("service.login");
			notificationApiPath = prop.getProperty("service.notification");
			calendarsApiPath = prop.getProperty("service.calendars");

			opu = new OpenPaasUser(prop.getProperty("user.username"), prop.getProperty("user.password"));

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
			return webServiceApi + calendarsApiPath + "/" + userId + "/events/" + uiId + ".ics?graceperiod=1";
		case NOTIFICATION:
			return webServiceApi + notificationApiPath;
		default:
			return null;
		}
	}

	public String getLoginApiPath() {
		return webServiceApi + loginApiPath;
	}

	public static void main(String[] args) throws Exception {
		// USED for test
		OpenPaasConnector opc = new OpenPaasConnector();

		List<String> attendeeList = new ArrayList<String>();
		attendeeList.add("user0@open-paas.org");
		attendeeList.add("user10@open-paas.org");

		Calendar cal = opc.getCalendarUtility().createCalendar("MyEventName", attendeeList, "admin@open-paas.org",
				"MyEventLocation");

		opc.wsCallGenerator(ActionActiviti.CALENDAR, cal);
	}

	private void prepareRequest(OpenPaasUser user, String ws) {
		HTTPBasicAuthFilter auth = new HTTPBasicAuthFilter(user.getUsername(), user.getPassword());
		ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
		config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

		client = ApacheHttpClient.create(config);
		client.addFilter(auth);
		webResource = client.resource(webServiceApi + ws);
	}

	public ApacheHttpClient login(OpenPaasUser user) {
		try {
			prepareRequest(user, loginApiPath);
			ClientResponse response = webResource.type(type).post(ClientResponse.class, user.generateJson());

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String resultJson = response.getEntity(String.class);
			userId = new JSONObject(resultJson).getString("_id");
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String wsCallGenerator(ActionActiviti action, Object request) {
		ApacheHttpClient client = login(opu);
		String json = null;
		TypeRequest type = TypeRequest.POST;
		switch (action) {
		case MAIL:
			// TODO Something..
			break;
		case CALENDAR:
			Calendar cal = (Calendar) request;
			json = Biweekly.parse(cal.toString()).first().writeJson();
			type = TypeRequest.PUT;
			uiId = ((VEvent) cal.getComponents().get(0)).getUid().getValue();
			break;
		case NOTIFICATION:
			NotificationOP notification = (NotificationOP) request;
			notification.setAuthor(userId);
			json = notification.generateJson();
			break;
		}
		return opc.wsOpCall(client, getPath(action), json, type);
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

	public OpenPaasUser getOpu() {
		return opu;
	}

}
