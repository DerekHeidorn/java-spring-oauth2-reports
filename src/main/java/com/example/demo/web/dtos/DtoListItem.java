package com.example.demo.web.dtos;

import java.util.Comparator;

public class DtoListItem implements Comparator<DtoListItem>, Comparable<DtoListItem> {

	private Integer itemId;
	
	private String label;


	public DtoListItem() {
		super();
	}

	public DtoListItem(Integer itemId, String label) {
		super();
		this.itemId = itemId;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int compare(DtoListItem o1, DtoListItem o2) {
		return o1.getLabel().compareTo(o2.getLabel());
	}

	@Override
	public int compareTo(DtoListItem o) {
		return this.getLabel().compareTo(o.getLabel());
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}





	
}
