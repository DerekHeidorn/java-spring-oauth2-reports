package com.example.demo.configuration.databind;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateTimeSerializer extends JsonSerializer<DateTime> {

	// DateTimeFormatter for the ISO8601 standard
	final static protected DateTimeFormatter DATETIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
	
	@Override
	public void serialize(DateTime datetime, JsonGenerator jg, SerializerProvider provider) throws IOException,	JsonProcessingException {
		jg.writeString(DATETIME_FORMAT.print(datetime));		
	}

}
