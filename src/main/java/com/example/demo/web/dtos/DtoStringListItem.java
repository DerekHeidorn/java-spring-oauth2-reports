package com.example.demo.web.dtos;

import java.util.Comparator;

public class DtoStringListItem implements Comparator<DtoStringListItem>, Comparable<DtoStringListItem> {

	private String itemId;
	
	private String itemLabel;


	public DtoStringListItem() {
		super();
		
	}

	public DtoStringListItem(String itemId, String itemLabel) {
		super();
		this.itemId = itemId;
		this.itemLabel = itemLabel;
	}

	@Override
	public int compare(DtoStringListItem o1, DtoStringListItem o2) {
		return o1.getItemLabel().compareTo(o2.getItemLabel());
	}

	@Override
	public int compareTo(DtoStringListItem o) {
		return this.getItemLabel().compareTo(o.getItemLabel());
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}



	
}
