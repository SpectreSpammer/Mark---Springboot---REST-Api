package com.onepieceofjava.SpringRestApiDemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="assets")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_seq")
	@SequenceGenerator(
			name = "asset_seq",
			sequenceName = "asset_sequence",
			initialValue = 201,
			allocationSize = 1
			)
	private Long id;
	private String name;
	private String type;
	private String serialNumber;
	
	@ManyToOne
	@JoinColumn(name ="employee_id")
	private Employee employee;
	
	
	
	public Asset() {
		
	}

	public Asset(Long id, String name, String type, String serialNumber) {
		
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
	
	public void setEmployee(Employee employee) {
	        this.employee = employee;
	 }
}
