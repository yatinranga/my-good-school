package com.nxtlife.mgs.util;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nxtlife.mgs.ex.ValidationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final TimeZone defaultTimeZone = TimeZone.getTimeZone("Asia/Calcutta");
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	public enum DayTime {
		START_OF_DAY, END_OF_DAY
	}

	public static Date getFormattedDate(String dateString, DayTime dayTime) {
		return convertStringToDate(dateString, defaultDateFormat, -330, dayTime);
	}

	public static Date convertStringToDate(String dateString, String format, Integer addMinutes, DayTime dayTime) {
		format = StringUtils.isEmpty(format) ? defaultDateFormat : format;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(dateString);

			if (addMinutes != null) {
				date = DateUtils.addMinutes(date, addMinutes);
			}

			if (dayTime != null) {
				switch (dayTime) {
				case START_OF_DAY:
					date = atStartOfDay(date);
					break;
				case END_OF_DAY:
					date = atEndOfDay(date);
					break;
				}
			}
			return date;
		} catch (ParseException e) {
			throw new ValidationException(String.format("Invalid date format correct format is : %s", defaultDateFormat));
		}
//		return null;
	}

	public static Date convertStringToDate(String dateString, Integer addMinutes, DayTime dayTime) {
		return convertStringToDate(dateString, null, addMinutes, dayTime);
	}

	public static Date convertStringToDate(String dateString, Integer addMinutes) {
		return convertStringToDate(dateString, null, addMinutes, null);
	}

	public static Date convertStringToDate(String dateString, String format) {
		return convertStringToDate(dateString, format, null, null);
	}

	public static Date convertStringToDate(String dateString) {
		return convertStringToDate(dateString, null, null, null);
	}

	public static String formatDate(Date date, String format, TimeZone timeZone) {
		if (date == null) {
			return "";
		}
		format = StringUtils.isEmpty(format) ? defaultDateFormat : format;
		timeZone = timeZone == null ? defaultTimeZone : timeZone;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(timeZone);
		return formatter.format(date);
	}

	public static String formatDate(Date date, String format) {
		return formatDate(date, format, null);
	}

	public static String formatDate(Date date) {
		return formatDate(date, null, null);
	}

	public static Date atStartOfDay(Date date) {
		return DateUtils.truncate(date, Calendar.DATE);
	}

	public static Date atEndOfDay(Date date) {
		return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
	}

}
