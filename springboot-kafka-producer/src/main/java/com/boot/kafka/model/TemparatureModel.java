package com.boot.kafka.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TemparatureModel {
	private final int id;
	private final String temparature;

	public TemparatureModel(@JsonProperty("id") final int id, @JsonProperty("temparature") final String temparature) {
		super();
		this.id = id;
		this.temparature = temparature;
	}

	public int getId() {
		return id;
	}

	public String getTemparature() {
		return temparature;
	}

}
