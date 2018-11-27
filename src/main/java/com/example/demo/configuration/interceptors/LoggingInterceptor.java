package com.example.demo.configuration.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoggingInterceptor implements HandlerInterceptor  {
	
	private Logger logger = LogManager.getLogger(this.getClass());	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {	
		
		// Just log PUT, POST, DELETE, GET
		if("PUT".equalsIgnoreCase(request.getMethod()) 
				|| "POST".equalsIgnoreCase(request.getMethod())
				|| "GET".equalsIgnoreCase(request.getMethod())
				|| "DELETE".equalsIgnoreCase(request.getMethod())) {
			//logger.info(request.getRequestURI() + " (" + request.getMethod() + ") " + UserUtil.getCurrentUser());
			logger.info(request.getRequestURI() + " (" + request.getMethod() + ") "); // TODO
		}
		return true;
	}
	@Override
	public void postHandle(	HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
	}
} 
