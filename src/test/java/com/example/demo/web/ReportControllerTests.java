package com.example.demo.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.configuration.WebConfiguration;
import com.example.demo.helpers.TestTokenAuthenticationHelper;
import com.example.demo.services.ReportManager;
import com.example.demo.services.ReportManager.ReportOutputType;
import com.example.demo.web.dtos.DtoReportCriteriaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class ReportControllerTests extends AbstractControllerTest {

	@Test
	public void testUnknownUri() throws Exception {

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/public/Unknown/uri")
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void testNoToken() throws Exception {

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/public/reports/list")
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void testUnauthorizedAuthorities() throws Exception {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS_FAKE");
		authorities.add("CUST_PROFILE_FAKE");
		
		String token = TestTokenAuthenticationHelper.createToken(TestTokenAuthenticationHelper.DEFAULT_PUBLIC_UUID, authorities);
		

		ResultActions ra = getMockMvc().perform(
							get("/api/v1.0/public/reports/list")
							.header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isForbidden());
		
		
		
	}
	
	@Test
	public void testAuthorizedInvalidUri() throws Exception {
		
		String token = TestTokenAuthenticationHelper.createDefaultPublicUnsubscribedToken();
		

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/Unknown/uri").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isNotFound());

	}
	
	@Test
	public void testReportListAuthorized() throws Exception {
		
		String token = TestTokenAuthenticationHelper.createDefaultPublicUnsubscribedToken();
		

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/public/reports/list").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isOk());
		
		ra.andExpect(jsonPath("data[0].itemId").value(ReportManager.ReportType.REPORT_USER_GROUPS.getReportCd()));
		ra.andExpect(jsonPath("data[1].itemId").doesNotExist());
		
	}
	
    protected String json(Object o) throws IOException {       
        ObjectMapper mapper = new ObjectMapper();
        WebConfiguration.applyCustomMappings(mapper);
        return mapper.writeValueAsString(o);

    }
	
    @Test
	public void testCreateReport_pdf() throws Exception {
    	
    	String token = TestTokenAuthenticationHelper.createDefaultPublicUnsubscribedToken();

    	
    	DtoReportCriteriaRequest reportCriteriaRequest = new DtoReportCriteriaRequest();
    	reportCriteriaRequest.setReportCd(ReportManager.ReportType.REPORT_USER_GROUPS.getReportCd());
    	reportCriteriaRequest.setStartDt(DateUtils.addDays(new Date(), -10));
    	reportCriteriaRequest.setEndDt(DateUtils.addDays(new Date(), -5));
    	reportCriteriaRequest.setReportOutputType(ReportOutputType.RPT_OUTPUT_TYPE_PDF.getMimeType().getExtension());
    	reportCriteriaRequest.setReportProcessType(ReportManager.ReportProcessType.RPT_PROCESS_TYPE_HTTP.getCode());
    	
    	String dataAsJson = json(reportCriteriaRequest); 
    	logger.debug(dataAsJson);

    	

    	// /public/reservations/{rsvnId}/details/permits (GET)
    	String jsonUrl = "/api/v1.0/public/reports/create";

    	ResultActions ra = getMockMvc().perform(
    			post(jsonUrl)
    			.header("Authorization", "bearer " + token)
				.accept(MediaType.parseMediaType("application/pdf"))
				.content(dataAsJson)
				.contentType(DEFAULT_CONTENT_TYPE))
    			.andDo(print());
				
    	//debugResultActions(ra);
    	
    	ra.andExpect(status().isCreated());

    	
    	MvcResult mvcResult = ra.andReturn();
    	int contentLength = mvcResult.getResponse().getContentLength();
    	
    	assertTrue(contentLength > 0);
    	
    	byte[] contentByteArray = mvcResult.getResponse().getContentAsByteArray();
    	assertTrue(contentByteArray.length > 0);
    	
    	String content = mvcResult.getResponse().getContentAsString();   
        
        String reportKey = JsonPath.read(content, "data.key");
        
        // /api/v1.0/public/reports/create/{key}
        String uri = "/api/v1.0/public/reports/create/" + reportKey + "?reportCd=" + ReportManager.ReportType.REPORT_USER_GROUPS.getReportCd()
         + "&reportOutputType=" + ReportOutputType.RPT_OUTPUT_TYPE_PDF.getMimeType().getExtension()
         + "&reportProcessType=" + ReportManager.ReportProcessType.RPT_PROCESS_TYPE_HTTP.getCode();
        
        		
        ra = getMockMvc().perform(
								get(uri)
								.header("Authorization", "bearer " + token)
								.accept(MediaType.parseMediaType("application/pdf"))
								)
        						// .andDo(print())
        						;
        mvcResult = ra.andReturn();
        
        byte[] pdf = mvcResult.getResponse().getContentAsByteArray();
        assertNotNull(pdf);
        String tempFilePath = System.getProperty("java.io.tmpdir") + "sample.pdf";
        assertNotNull(tempFilePath);
        logger.info("tempFilePath=" + tempFilePath);
        FileUtils.writeByteArrayToFile(new File(tempFilePath), pdf);
    	
    	// 

	}  
	
    @Test
	public void testCreateReport_pdf_email() throws Exception {
    	
    	String token = TestTokenAuthenticationHelper.createDefaultPublicUnsubscribedToken();

    	
    	DtoReportCriteriaRequest reportCriteriaRequest = new DtoReportCriteriaRequest();
    	reportCriteriaRequest.setReportCd(ReportManager.ReportType.REPORT_USER_GROUPS.getReportCd());
    	reportCriteriaRequest.setStartDt(DateUtils.addDays(new Date(), -10));
    	reportCriteriaRequest.setEndDt(DateUtils.addDays(new Date(), -5));
    	reportCriteriaRequest.setReportOutputType(ReportOutputType.RPT_OUTPUT_TYPE_PDF.getMimeType().getExtension());
    	reportCriteriaRequest.setReportProcessType(ReportManager.ReportProcessType.RPT_PROCESS_TYPE_EMAIL.getCode());
    	
    	String dataAsJson = json(reportCriteriaRequest); 
    	logger.debug(dataAsJson);

    	

    	// /public/reservations/{rsvnId}/details/permits (GET)
    	String jsonUrl = "/api/v1.0/public/reports/create";

    	ResultActions ra = getMockMvc().perform(
    			post(jsonUrl)
    			.header("Authorization", "bearer " + token)
				.accept(MediaType.parseMediaType("application/pdf"))
				.content(dataAsJson)
				.contentType(DEFAULT_CONTENT_TYPE))
    			.andDo(print());
				
    	//debugResultActions(ra);
    	
    	ra.andExpect(status().isCreated());

    	
    	
    	ra.andExpect(jsonPath("globalInfoMsgs[0]").value("File Sent"));


	}  
    
}
