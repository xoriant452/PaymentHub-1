package com.citi.paymenthub.kafka.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {

	public static Object getInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	
	public static List<String> convertJsonStringToList(String idList)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(idList, new TypeReference<List<String>>() {
		});
	}
	
	public static Map<Long,String> getLastSevenDays(){
		
		Map<Long,String> lastSevenDaysMap=new TreeMap<Long,String>(Collections.reverseOrder());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for(int i=0;i>-7;i--) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, i);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		lastSevenDaysMap.put(cal.getTimeInMillis(), dateFormat.format(cal.getTime()).substring(0, 10));
		}
		return lastSevenDaysMap;
	}
	
public static Long getCurrentDayMilisecond(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

public static void getCurrentTimeUsingDate() {
    Date date = new Date();
    String strDateFormat = "dd/MM/yyyy";
    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
    String formattedDate= dateFormat.format(date);
    System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
}
public static void main(String[] args) {
	getCurrentTimeUsingDate();
}
}
