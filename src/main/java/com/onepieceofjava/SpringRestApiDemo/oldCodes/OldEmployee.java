package com.onepieceofjava.SpringRestApiDemo.oldCodes;

import java.util.ArrayList;
import java.util.List;

public class OldEmployee {

	private Long id;
	private String name;
	private String department;
	private List<OldAsset> assets;
	
	
	
	public OldEmployee() {
		this.assets = new ArrayList<>();
	}

	public OldEmployee(Long id, String name, String department) {
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

	public List<OldAsset> getAssets() {
		return assets;
	}

	public void setAssets(List<OldAsset> assets) {
		this.assets = assets;
	}
	
	public void addAsset(OldAsset asset) {
		this.assets.add(asset);
	}
	
	public void removeAsset(OldAsset asset) {
		this.assets.remove(asset);
	}
	
	
}
