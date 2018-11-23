package com.example.demo;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.helpers.TestTokenAuthenticationHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReportsApplicationTests {
	
	public final static String DEFAULT_PUBLIC_UUID = "c95802ac-e465-11e8-9f32-f2801f1b9fd1";  // Joe.Customer@foo.com.invali
	public final static String DEFAULT_PUBLIC_CLIENT_ID = "CLTID-Zeq1LRso5q-iLU9RKCKnu";

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void contextLoads() throws Exception {
		ResultActions ra = mockMvc.perform(
						get("/user")
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isOk());
		
	}

	@Test
	public void contextLoads2() throws Exception {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS");
		authorities.add("CUST_PROFILE");
		
		String token = TestTokenAuthenticationHelper.createToken(DEFAULT_PUBLIC_UUID, authorities);
		
		loginPublicUser(DEFAULT_PUBLIC_UUID);
		ResultActions ra = mockMvc.perform(
						get("/api/v1.0/user").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isOk());
		
	}
	
	@Test
	public void getUsername() throws Exception {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS");
		authorities.add("CUST_PROFILE");
		
		String token = TestTokenAuthenticationHelper.createToken(DEFAULT_PUBLIC_UUID, authorities);
		
		loginPublicUser(DEFAULT_PUBLIC_UUID);
		ResultActions ra = mockMvc.perform(
						get("/username").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isOk());
		
	}
	
	@Test
	public void getUsername2() throws Exception {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS2");
		authorities.add("CUST_PROFILE2");
		
		String token = TestTokenAuthenticationHelper.createToken(DEFAULT_PUBLIC_UUID, authorities);
		
		loginPublicUser(DEFAULT_PUBLIC_UUID);
		ResultActions ra = mockMvc.perform(
						get("/username").header("Authorization", "bearer " + token)
						).andDo(print());
		
		debugResultActions(ra);
		
		ra.andExpect(status().isForbidden());
		
	}
	
//	   @Test
//	   public void shouldGenerateAuthToken() throws Exception {
//	       String token = TokenAuthenticationService.createToken("john");
//
//	       assertNotNull(token);
//	       mvc.perform(MockMvcRequestBuilders.get("/test").header("Authorization", token)).andExpect(status().isOk());
//	   }
	
    protected void debugResultActions(ResultActions ra) throws UnsupportedEncodingException {
    	
    	MvcResult mvcResult = ra.andReturn();
        String content = mvcResult.getResponse().getContentAsString();   
        System.out.println("Response:\n"  + content + "\n");
    	
    } 
    
   public void loginPublicUser(String userUuid) {

//    	OAuth2Request(
//    			Map<String,String> requestParameters, 
//    			String clientId, 
//    			Collection<? extends org.springframework.security.core.GrantedAuthority> authorities, 
//    			boolean approved, 
//    			Set<String> scope, 
//    			Set<String> resourceIds, 
//    			String redirectUri, 
//    			Set<String> responseTypes,
//    			Map<String,Serializable> extensionProperties) 
    	
    	// TODO 3.0  Creating Authentication objects to make it work with unit tests.  Brute force coding to make it work. 
    	// Note, maybe use DefaultOAuth2RequestFactory to create the OAuth2Request?
    	Map<String,String> requestParameters = new HashMap<String,String>();
    	String clientId = OAuth2Utils.CLIENT_ID;
    	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	authorities.add(new Authority("CUST_ACCESS"));
    	authorities.add(new Authority("CUST_PROFILE"));
    	boolean approved = true;
    	
    	Set<String> scope = new HashSet<String>();
    	scope.add("public");
    	
    	Set<String> resourceIds = new HashSet<String>();
    	String redirectUri = null;    	
    	Set<String> responseTypes = new HashSet<String>();    	
    	Map<String,Serializable> extensionProperties = new HashMap<String,Serializable>();
    	
    	
    	OAuth2Request storedRequest = new OAuth2Request(requestParameters, clientId, authorities, approved, scope, resourceIds, redirectUri, responseTypes, extensionProperties);
    	Authentication userAuthentication = new TestAppAuthentication(userUuid, authorities, null, userUuid, userUuid);
    	
    	// create OAuth2 Authentication
    	OAuth2Authentication oauth2 = new OAuth2Authentication(storedRequest, userAuthentication);
    	
    	// add authentication to the Spring Security Context holder
    	SecurityContextHolder.getContext().setAuthentication(oauth2);	
    	
    	//publicTokenSecurityManager.loadUserByUsername(tbUser.getUserLogin());
    	
    }
   
   private class Authority implements GrantedAuthority {
	   
	   	private String name;

		public Authority(String name) {
			super();
			this.name = name;
		}
	
		@Override
		public String getAuthority() {
			// TODO Auto-generated method stub
			return null;
		}
	   
   }
   

   

   
   private class TestJwtAccessTokenConverter extends JwtAccessTokenConverter {
	  
	    @Override
	    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
	                                     OAuth2Authentication oAuth2Authentication) {
//	        log.info("Jwt Access Token converter enhances access token.");
	        //User user = (User)oAuth2Authentication.getUserAuthentication().getPrincipal();

	        //Map<String, Object> info = new LinkedHashMap<>(
	        //        accessToken.getAdditionalInformation());

	        //info.put("userId", user.getUserId());
	        //info.put("firstNm", user.getFirstNm());
	        ///info.put("lastNm", user.getLastNm());
	        //info.put("isStaff", user.isStaff());
	        //info.put("preferredCampgroundId", user.getPreferredCampgroundId());

	        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
	        //customAccessToken.setAdditionalInformation(info);
	        return super.enhance(customAccessToken, oAuth2Authentication);
	    }
   }
   
   private class TestAppAuthentication implements Authentication {

	   
	   	private String name;
	   	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	   	private Object credentials;
	   	private Object details;
	   	private Object principal;

		public TestAppAuthentication(String name, Collection<GrantedAuthority> authorities, Object credentials,
				Object details, Object principal) {
			super();
			this.name = name;
			this.authorities = authorities;
			this.credentials = credentials;
			this.details = details;
			this.principal = principal;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public Object getCredentials() {
			return credentials;
		}

		@Override
		public Object getDetails() {
			return details;
		}

		@Override
		public Object getPrincipal() {
			return principal;
		}

		@Override
		public boolean isAuthenticated() {
			return true;
		}

		@Override
		public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		}
	   
  }
	
}
