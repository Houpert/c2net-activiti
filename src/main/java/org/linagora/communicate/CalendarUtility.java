package org.linagora.communicate;

import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.linagora.utility.DateManager;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class CalendarUtility {

	private static final String MAP_NAME = "name";
	private static final String MAP_LOCATION = "location";
	private static final String MAP_ATTENDEE = "attendee";
	private final int ADD_HOURS = 2;

	public Calendar createCalendar(String name, List<String> attendeeList, String organizerName, String location)
			throws SocketException, ParseException, URISyntaxException {

		// Default setting VCalendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(Version.VERSION_2_0);

		// Initialize date VEvent
		DateTime start = new DateTime(DateManager.today());
		DateTime end = new DateTime(DateManager.addHours(ADD_HOURS));
		CalendarBuilder builder = new CalendarBuilder();
		TimeZoneRegistry registry = builder.getRegistry();
		TimeZone tz = registry.getTimeZone("Europe/Berlin"); // TODO
		start.setTimeZone(tz);
		end.setTimeZone(tz);

		// Initialize VEvent
		VEvent event = new VEvent(start, end, name);
		Uid uid = new Uid(UUID.randomUUID().toString());
		event.getProperties().add(uid);
		event.getProperties().add(Transp.OPAQUE);
		event.getProperties().add(new Location(location));

		// Generate user to the event
		Organizer organizer = new Organizer(URI.create("mailto:" + organizerName));
		organizer.getParameters().add(new Cn(organizerName));

		event.getProperties().add(organizer);
		for (String attendeeMail : attendeeList) {
			Attendee att = new Attendee(URI.create("mailto:" + attendeeMail));
			att.getParameters().add(Role.REQ_PARTICIPANT);
			att.getParameters().add(new Cn(attendeeMail));
			att.getParameters().add(new Rsvp(true));
			att.getParameters().add(PartStat.NEEDS_ACTION);
			event.getProperties().add(att);
		}
		calendar.getComponents().add(event);
		return calendar;
	}

	public Calendar createCalendarFromJson(Map<String, Object> map, String organizerName) throws Exception {
		Calendar cal;
		try {
			String name = (String) map.get(MAP_NAME);
			String location = (String) map.get(MAP_LOCATION);
			List<String> attendeeList = (List<String>) map.get(MAP_ATTENDEE);

			if (name == null || attendeeList == null) {
				throw new Exception("An event name or attendee list is needed");
			}

			cal = createCalendar(name, attendeeList, organizerName, location);
			return cal;
		} catch (Exception e) {
			throw e;
		}
	}

}
