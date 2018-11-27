package com.example.demo.helpers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TestTokenAuthenticationHelper {

	public final static String DEFAULT_PUBLIC_UUID = "c95802ac-e465-11e8-9f32-f2801f1b9fd1";  // Joe.Customer@foo.com.invali
	public final static String DEFAULT_PUBLIC_SUBSCRIBED_UUID = "14468f27-44e8-4fc3-8cc6-3a48c80fd5aa";  // Joe.Subscribed@foo.com.invali
	public final static String DEFAULT_PUBLIC_GROUP_SUBSCRIBED_UUID = "d71b920a-04a9-44d3-beda-a736601a64c5";  // Joe.Group.Subscribed@foo.com.invali
	public final static String DEFAULT_PUBLIC_CLIENT_ID = "CLTID-Zeq1LRso5q-iLU9RKCKnu";
	
	public final static String DEFAULT_ADMIN_UUID = "c957fece-e465-11e8-9f32-f2801f1b9fd1";  // sys.admin@foo.com.invali
	
    static final long EXPIRATIONTIME = 24 * 60 * 60; // 1 day
    static final String SECRET = "BMcrqdcd7QeEmR8CXyU";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {

        String jwt = createToken(username, new ArrayList<String>());

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) :
                        null;
        }
        return null;
    }

    public static String createDefaultPublicUnsubcribedToken() {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS");
		authorities.add("CUST_RPTS");
		
    	return createToken(DEFAULT_PUBLIC_UUID, authorities);
    }
    
    public static String createDefaultPublicSubcribedToken() {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS");
		authorities.add("CUST_RPTS");
		authorities.add("CUST_RPTS_SUB");
		
    	return createToken(DEFAULT_PUBLIC_UUID, authorities);
    }
    
    public static String createDefaultPublicGroupSubcribedToken() {
		List<String> authorities = new ArrayList<>();
		authorities.add("CUST_ACCESS");
		authorities.add("CUST_RPTS");
		authorities.add("CUST_RPTS_SUB");
		authorities.add("CUST_RPTS_GROUP_SUB");
		
    	return createToken(DEFAULT_PUBLIC_UUID, authorities);
    }
    
    public static String createDefaultAdmniToken() {
		List<String> authorities = new ArrayList<>();
		authorities.add("ADM_ACCESS");
		authorities.add("ADM_RPTS");
		
    	return createToken(DEFAULT_ADMIN_UUID, authorities);
    }
    
    public static String createToken(String userUuid, List<String> authorities) {
    	//io.jsonwebtoken.JwtBuilder().
    	String base64 = null;
    	try {
    		base64 = new String(Base64.getEncoder().encode(SECRET.getBytes()));
    	} catch(Exception e) {
    		
    	}
    	UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
    	
        JwtBuilder b = Jwts.builder();
        b.setSubject(userUuid);
        b.setIssuedAt(new Date());
        b.claim(AccessTokenConverter.JTI, randomUUIDString);
        b.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME));
        b.claim(AccessTokenConverter.AUTHORITIES, authorities);
        b.signWith(SignatureAlgorithm.HS512, base64);
        String jwt = b.compact();

        return jwt;
    }
}
