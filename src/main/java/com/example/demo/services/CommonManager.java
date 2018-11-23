package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.persist.CommonDao;

@Service
public class CommonManager {

	@Autowired
	private CommonDao commonDao;
	
	public String getConfigParamValue(String key){
		return commonDao.getConfigParamValue(key);
	}
	
}
