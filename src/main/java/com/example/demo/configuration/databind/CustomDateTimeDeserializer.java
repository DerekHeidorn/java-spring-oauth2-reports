package com.example.demo.configuration.databind;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateTimeDeserializer extends JsonDeserializer<DateTime> {

	// DateTimeFormatter for the ISO8601 standard
	final static protected DateTimeFormatter DATETIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
	
	@Override
	public DateTime deserialize(JsonParser jsonParser, DeserializationContext dc) throws IOException, JsonProcessingException {
		String date = jsonParser.getText();
		
		try {
			return DATETIME_FORMAT.parseDateTime(date);
		} catch(IllegalArgumentException iae) {
			throw new IOException("Invalid DateTime Format: " + date);
		}
	}
}
