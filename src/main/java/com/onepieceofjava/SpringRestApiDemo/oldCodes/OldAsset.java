package com.onepieceofjava.SpringRestApiDemo.oldCodes;

public class OldAsset {
	
	private Long id;
	private String name;
	private String type;
	private String serialNumber;
	
	public OldAsset(Long id, String name, String type, String serialNumber) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.serialNumber = serialNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	

	
}
