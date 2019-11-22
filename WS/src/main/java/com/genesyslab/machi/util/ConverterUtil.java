package com.genesyslab.machi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.genesyslab.machi.helper.MachiConstants;

@Component
public class ConverterUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterUtil.class);

	static SimpleDateFormat format = new SimpleDateFormat(MachiConstants.PARAM_DATE_FORMAT);
	static SimpleDateFormat timeFormat = new SimpleDateFormat(MachiConstants.PARAM_TIME_FORMAT);

	public String toJson(String message) {
		if (StringUtils.isNotBlank(message)) {
			message = "{\"message\": \"" + message + "\"}";
		}
		return message;
	}

	public Date toDate(String dateStr) {
		if (StringUtils.isNotBlank(dateStr)) {
			try {
				return format.parse(dateStr);
			} catch (ParseException ex) {
				LOGGER.warn("toDate() | ParseException caught for date: {} ", dateStr, ex);
			}
		}
		return new Date();
	}

	public String toDateString(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (null != date) {
			calendar.setTime(date);
		}
		return calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.YEAR);
	}

	public Date toTime(String timeStr) {
		if (StringUtils.isNotBlank(timeStr)) {
			try {
				return timeFormat.parse(timeStr);
			} catch (ParseException ex) {
				LOGGER.warn("toTime() | ParseException caught for date: {} ", timeStr, ex);
			}
		}
		return new Date();
	}

	public boolean toBoolean(String booleanStr) {
		return BooleanUtils.toBoolean(booleanStr);
	}
}
