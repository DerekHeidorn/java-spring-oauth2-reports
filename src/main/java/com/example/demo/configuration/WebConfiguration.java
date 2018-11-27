package com.example.demo.configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.configuration.databind.CustomDateDeserializer;
import com.example.demo.configuration.databind.CustomDateSerializer;
import com.example.demo.configuration.databind.CustomDateTimeDeserializer;
import com.example.demo.configuration.databind.CustomDateTimeSerializer;
import com.example.demo.configuration.databind.StringTrimmerDeserializer;
import com.example.demo.configuration.interceptors.LoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
@ComponentScan("com.example.demo.services")
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource messageSource() {
    	ResourceBundleMessageSource messageResource = new ResourceBundleMessageSource();
    	String[] resources= {"messages-api","errors-api"};
    	
    	messageResource.setBasenames(resources);
    	return messageResource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
    } 
    
//    @Bean
//    public MultipartResolver multipartResolver() {
//        return new CommonsMultipartResolver();
//    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//converters.clear();
		converters.add(xmlConverter());
        converters.add(jsonConverter());

    }

	@Bean(name="jsonConverter")
	public MappingJackson2HttpMessageConverter jsonConverter() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

		ObjectMapper ob = builder.build();
		applyCustomMappings(ob);

		// add JSONP converter
		return new MappingJackson2HttpMessageConverter(ob);
	}

	@Bean(name="xmlConverter")
	public Jaxb2RootElementHttpMessageConverter xmlConverter() {
		Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_XML));
		return converter;
	}

    public static void applyCustomMappings(ObjectMapper om) {

		SimpleModule module = new SimpleModule();

		LogFactory.getLog(WebConfiguration.class).info("Adding Custom Date/Time Serializers...");
		module.addSerializer(Date.class, new CustomDateSerializer());
		module.addSerializer(DateTime.class, new CustomDateTimeSerializer());
		
		LogFactory.getLog(WebConfiguration.class).info("Adding Custom Date/Time Deserializers...");
		module.addDeserializer(Date.class, new CustomDateDeserializer());
		module.addDeserializer(DateTime.class, new CustomDateTimeDeserializer());

		LogFactory.getLog(WebConfiguration.class).info("Adding Custom String Deserializer...");
		module.addDeserializer(String.class, new StringTrimmerDeserializer());		
		
		om.registerModule(module);            	
    	
    }

}
