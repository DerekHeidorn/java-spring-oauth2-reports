package com.example.demo.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
	private Logger logger = LogManager.getLogger(this.getClass());
	
    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
    	DefaultTokenServices tokenServices = tokenServices();
        config.tokenServices(tokenServices);
    }
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**","/js/**","/resource/**");
//    }
 
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); 
		http.cors();
		
		http.authorizeRequests()
    				.antMatchers("/api/v1.0/reports/download/**")
    				.permitAll()
    					.and()
    						.anonymous().
    					and()
    						.cors().disable();
		http.authorizeRequests()
    				.antMatchers("/api/v1.0/public/**").authenticated();
    	http.authorizeRequests()
    				.antMatchers("/api/v1.0/admin/**").authenticated();

	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		logger.info("Getting corsConfigurationSource...");
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:4200", "http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("OPTIONS","GET","POST", "PUT"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
    
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
		final String alg = "HMACSHA512";
		final String key = "BMcrqdcd7QeEmR8CXyU";
		SecretKey secretKey = new SecretKeySpec(key.getBytes(), alg);
    	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    	converter.setSigner(new MacSigner(alg, secretKey));
    	converter.setVerifier(new MacSigner(alg, secretKey));
    	converter.setJwtClaimsSetVerifier(new CustomClaimVerifier());
        return converter;
    }
 
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    public class CustomClaimVerifier implements JwtClaimsSetVerifier {
        @Override
        public void verify(Map<String, Object> claims) throws InvalidTokenException {
            Object authObj = claims.get(AccessTokenConverter.AUTHORITIES);
            if(authObj instanceof List) {
            	List<?> auths = (List<?>) authObj;
            	
                if ((auths == null) || (auths.size() == 0)) {
                    throw new InvalidTokenException("authorities are required");
                }
            }

        }
    }


    
 
}
