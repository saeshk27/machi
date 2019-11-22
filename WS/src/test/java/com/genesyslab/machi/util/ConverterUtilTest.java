package com.genesyslab.machi.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import com.genesyslab.machi.helper.MachiTestConstants;

public class ConverterUtilTest {

	@InjectMocks
	ConverterUtil converterUtil = new ConverterUtil();

	@Test
	public void testShouldReturnJson() {
		String json = converterUtil.toJson(MachiTestConstants.TEST_JSON_MESSAGE);

		assertNotNull(json);
		assertTrue(json.contains(MachiTestConstants.TEST_JSON_MESSAGE));
		assertTrue(json.contains(MachiTestConstants.PARAM_MESSAGE));
	}

	@Test
	public void testShouldReturnNull() {
		String json = converterUtil.toJson(null);

		assertNull(json);
	}

	@ParameterizedTest
	@ValueSource(strings = { "True", "YeS", "tRuE", "true", "on", "y", "t", "yes" })
	public void testShouldReturnBooleanTrue(String value) {
		boolean response = converterUtil.toBoolean("True");

		assertTrue(response);
	}

	@ParameterizedTest
	@ValueSource(strings = { "FalSE", "nO", "false", "n", "f", "junk word", " " })
	public void testShouldReturnBooleanFalse(String value) {
		boolean response = converterUtil.toBoolean("True");

		assertTrue(response);
	}

	@Test
	public void testShouldReturnDate() {
		Date date = converterUtil.toDate(MachiTestConstants.TEST_DATE);

		assertNotNull(date);
		assertTrue(date.before(new Date()));

	}

	@Test
	public void testShouldReturnNewDateWhenWrongFormat() {
		Date currentDate = new Date();
		Date date = converterUtil.toDate(MachiTestConstants.TEST_BAD_DATE);

		assertNotNull(date);
		assertEquals(date.toString().substring(0, 10), currentDate.toString().substring(0, 10));
	}

	@Test
	public void testShouldReturnDateString() {
		String dateStr = converterUtil.toDateString(MachiTestConstants.TEST_TODAY_DATE);

		validateDate(dateStr);
	}

	@Test
	public void testShouldReturnDateStringWhenNull() {
		String dateStr = converterUtil.toDateString(null);

		validateDate(dateStr);
	}

	@SuppressWarnings("deprecation")
	private void validateDate(String dateStr) {
		assertNotNull(dateStr);
		assertTrue(dateStr.contains("" + (MachiTestConstants.TEST_TODAY_DATE.getYear() + 1900)));
		assertTrue(dateStr.contains("" + (MachiTestConstants.TEST_TODAY_DATE.getMonth() + 1)));
		assertTrue(dateStr.contains("" + MachiTestConstants.TEST_TODAY_DATE.getDate()));
	}

	@Test
	public void testShouldReturnTime() {
		Date time = converterUtil.toTime(MachiTestConstants.TEST_TIME);

		assertNotNull(time);
		assertTrue(time.before(new Date()));
	}

	@Test
	public void testShouldReturnNewTimeWhenWrongFormat() {
		Date currentTime = new Date();
		Date time = converterUtil.toTime(MachiTestConstants.TEST_BAD_TIME);

		assertNotNull(time);
		assertEquals(time.toString().substring(0, 10), currentTime.toString().substring(0, 10));
	}
}
