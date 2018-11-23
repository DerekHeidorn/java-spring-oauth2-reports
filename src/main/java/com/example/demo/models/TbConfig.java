package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "tb_config", schema="public")
public class TbConfig {

	@Id
	@Column(name="cfgprm_key")
	private String key;

	@Column(name="cfgprm_de")
	private String description;

	@Column(name="cfgprm_val")
	private String value;

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public String getValue() {
		return value;
	}



	
}
