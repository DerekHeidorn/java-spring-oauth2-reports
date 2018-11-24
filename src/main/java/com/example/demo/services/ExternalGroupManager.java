package com.example.demo.services;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalGroupManager {
	protected final transient Log logger = LogFactory.getLog(getClass());	
	
	

	public Group[] getGroups(String bearerToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", bearerToken);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<ResponseWrapper> respEntity = restTemplate.exchange("http://127.0.0.1:9001/api/v1.0/public/group", HttpMethod.GET, entity, ResponseWrapper.class);

		ResponseWrapper groupResponse = respEntity.getBody();

        logger.info(groupResponse.toString());
        logger.debug(groupResponse.getData().toString());
        for(Group g: groupResponse.getData()) {
        	logger.debug("g=" + g);
        }
        
        return groupResponse.getData();
	}
	
}
