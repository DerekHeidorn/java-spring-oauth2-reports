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

import com.example.demo.services.models.ResponseWrapper;
import com.example.demo.services.models.groups.Group;

@Service
public class ExternalGroupManager {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private Environment environment;

	public Group[] getGroups(String bearerToken) {
		
		String groupApiUrl = environment.getRequiredProperty("REPORT_APP_GROUP_API_URL_V1");
		logger.info("groupApiUrl=" + groupApiUrl);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", bearerToken);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<ResponseWrapper> respEntity = restTemplate.exchange(groupApiUrl + "/public/group", HttpMethod.GET, entity, ResponseWrapper.class);

		ResponseWrapper groupResponse = respEntity.getBody();

        logger.info(groupResponse.toString());
        logger.debug(groupResponse.getData().toString());
        for(Group g: groupResponse.getData()) {
        	logger.debug("g=" + g);
        }
        
        return groupResponse.getData();
	}
	
	public Group[] getGroupDetail(String bearerToken, String groupUuid) {
		
		String groupApiUrl = environment.getRequiredProperty("REPORT_APP_GROUP_API_URL_V1");
		logger.info("groupApiUrl=" + groupApiUrl);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", bearerToken);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();

		// // /api/v1.0/public/group/detail/<group_uuid>
		ResponseEntity<ResponseWrapper> respEntity = restTemplate.exchange(groupApiUrl + "/public/group/detail/" + groupUuid, 
																			HttpMethod.GET, entity, ResponseWrapper.class);

		ResponseWrapper groupResponse = respEntity.getBody();

        logger.info(groupResponse.toString());
        logger.debug(groupResponse.getData().toString());
        for(Group g: groupResponse.getData()) {
        	logger.debug("g=" + g);
        }
        
        return groupResponse.getData();
	}
	
	



	public Environment getEnvironment() {
		return environment;
	}



	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
}
