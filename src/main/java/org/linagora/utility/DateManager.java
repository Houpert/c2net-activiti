package org.linagora.utility;

import java.util.Calendar;
import java.util.Date;

public class DateManager {

	public static Date addHours(int hours){
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(new Date());
		calDate.add(Calendar.HOUR, hours);
		return calDate.getTime();
	}
	
	public static Date today(){
		return new Date();
	}
	
}
