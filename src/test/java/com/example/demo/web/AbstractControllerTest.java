package com.example.demo.web;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AbstractControllerTest {
	
	protected final transient Log log = LogFactory.getLog(getClass());	

    static {
    	System.setProperty("REPORT_APP_USER_API_URL_V1", "http://127.0.0.1:9000/api/v1.0");
        System.setProperty("REPORT_APP_GROUP_API_URL_V1", "http://127.0.0.1:9001/api/v1.0");
        System.setProperty("REPORT_APP_AES_SECRET_KEY", "OucwXmUyIDJaHGeH");
        System.setProperty("REPORT_APP_MD5_SECRET_KEY", "ka8BnRJybDh9gRZZ292OjwByVUGFYMKQcB9QcW");

        System.setProperty("REPORT_APP_SMTP_HOST", "localhost");
        System.setProperty("REPORT_APP_SMTP_PORT", "25");
        System.setProperty("REPORT_APP_EMAIL_DEFAULT_SENDER", "support@example.com");
        
        //defaultESenderAddr
    }

	
	final protected static MediaType DEFAULT_CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype());
	
	@Autowired
	private MockMvc mockMvc;

	
    protected void debugResultActions(ResultActions ra) throws UnsupportedEncodingException {
    	
    	MvcResult mvcResult = ra.andReturn();
        String content = mvcResult.getResponse().getContentAsString();   
        log.debug("Response:\n"  + content + "\n");
    	
    } 
	
	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
}
