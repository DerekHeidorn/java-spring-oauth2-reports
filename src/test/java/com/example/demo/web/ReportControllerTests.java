package com.example.demo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.helpers.TestTokenAuthenticationHelper;
import com.example.demo.services.ReportManager;

public class ReportControllerTests extends AbstractControllerTest {

	@Test
	public void testUnknownUri() throws Exception {

		ResultActions ra = getMockMvc().perform(
						get("/Unknown/uri")
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void testNoToken() throws Exception {

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/public/report/list")
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
						get("/api/v1.0/public/report/list").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isForbidden());
		
		
		
	}
	
	@Test
	public void testReportListAuthorized() throws Exception {
		
		String token = TestTokenAuthenticationHelper.createDefaultPublicUnsubcribedToken();
		

		ResultActions ra = getMockMvc().perform(
						get("/api/v1.0/public/report/list").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isOk());
		
		ra.andExpect(jsonPath("data[0].itemId").value(ReportManager.ReportType.REPORT_GROUPS.getReportCd()));
		ra.andExpect(jsonPath("data[1].itemId").doesNotExist());
		
	}
	

	
}
