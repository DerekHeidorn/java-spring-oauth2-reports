package com.example.demo.configuration.databind;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.services.utils.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

	final static protected SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DateUtil.DATE_FORMAT);
	
	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext dc) throws IOException, JsonProcessingException {

		String date = jsonParser.getText();
		try {
			return DATE_FORMAT.parse(date);
		} catch (ParseException e) {			
			throw new IOException("Invalid Date Format: " + jsonParser.getText());
		}	

	}

}
