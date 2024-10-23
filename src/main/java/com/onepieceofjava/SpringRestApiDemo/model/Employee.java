package com.onepieceofjava.SpringRestApiDemo.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {

	private Long id;
	private String name;
	private String department;
	private List<Asset> assets;
	
	
	
	public Employee() {
		this.assets = new ArrayList<>();
	}

	public Employee(Long id, String name, String department) {
		super();
		this.id = id;
		this.name = name;
		this.department = department;
		this.assets = new ArrayList<>();
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	
	public void addAsset(Asset asset) {
		this.assets.add(asset);
	}
	
	public void removeAsset(Asset asset) {
		this.assets.remove(asset);
	}
	
	
}
