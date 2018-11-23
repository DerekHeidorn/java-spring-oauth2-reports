package com.example.demo.web.dtos;

import java.util.Comparator;

public class DtoStringListItem implements Comparator<DtoStringListItem>, Comparable<DtoStringListItem> {

	private String itemId;
	
	private String label;


	public DtoStringListItem() {
		super();
		
	}

	public DtoStringListItem(String itemId, String label) {
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
	public int compare(DtoStringListItem o1, DtoStringListItem o2) {
		return o1.getLabel().compareTo(o2.getLabel());
	}

	@Override
	public int compareTo(DtoStringListItem o) {
		return this.getLabel().compareTo(o.getLabel());
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}



	
}
