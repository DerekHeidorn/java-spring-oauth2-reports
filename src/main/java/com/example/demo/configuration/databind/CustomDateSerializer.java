package com.example.demo.configuration.databind;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.services.utils.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateSerializer extends JsonSerializer<Date> {

	final static protected SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DateUtil.DATE_FORMAT);
	
	@Override
	public void serialize(Date date, JsonGenerator jg, SerializerProvider provider) throws IOException,	JsonProcessingException {		
		jg.writeString(DATE_FORMAT.format(date));		
	}

}
