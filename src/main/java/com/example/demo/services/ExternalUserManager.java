package com.example.demo.services;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.services.models.UserResponseWrapper;
import com.example.demo.services.models.persons.User;

@Service
public class ExternalUserManager {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private Environment environment;
	
	
	// /api/v1.0/public/account/profile/
	public User getUserProfile(String bearerToken) {
		
		String userApiUrl = environment.getRequiredProperty("REPORT_APP_USER_API_URL_V1");
		logger.info("userApiUrl=" + userApiUrl);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", bearerToken);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<UserResponseWrapper> respEntity = restTemplate.exchange(userApiUrl + "/public/account/profile/", HttpMethod.GET, entity, UserResponseWrapper.class);

		UserResponseWrapper userResponse = respEntity.getBody();

        logger.info(userResponse.toString());
        logger.debug(userResponse.getData().toString());
        User user = userResponse.getData();
        return user;
	}
	
}
