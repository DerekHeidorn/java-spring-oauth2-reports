package com.example.demo.web.dtos;

import java.util.Comparator;

public class DtoCodeTableItem implements Comparator<DtoCodeTableItem>, Comparable<DtoCodeTableItem> {

	private String code;
	
	private String description;
	
	private boolean isDefault = false;


	public DtoCodeTableItem() {
		super();
		
	}

	public DtoCodeTableItem(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}


	@Override
	public int compare(DtoCodeTableItem o1, DtoCodeTableItem o2) {
		return o1.getDescription().compareTo(o2.getDescription());
	}

	@Override
	public int compareTo(DtoCodeTableItem o) {
		return this.getDescription().compareTo(o.getDescription());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}





	
}
