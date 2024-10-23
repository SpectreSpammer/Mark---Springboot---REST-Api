package com.onepieceofjava.SpringRestApiDemo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	private List<Employee> employees = new ArrayList<>();
	
	private List<Asset> assets = new ArrayList<>();
	
	private Long employeeId = 101L;
	
	private Long assetId = 201L;
	
	//For employees
	//get All
	@GetMapping("employees")
	public List<Employee> getAllEmployees(){
		return employees;
	}
	
	//get by Id
	@GetMapping("employees/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employees.stream()
				.filter(emp -> emp.getId().equals(id))
				.findFirst()
				.orElse(null);
	}
	
	//Adding of employees
	
	@PostMapping("/employees")
	public Employee addEmployee(@RequestBody Employee employee) {
		
		employee.setId(employeeId++);
		
		if(employee.getAssets() != null && !employee.getAssets().isEmpty()) {
			List<Asset> processedAssets = new ArrayList<>();
			for( Asset asset : employee.getAssets()) {
				asset.setId(assetId);
				assets.add(asset);
				processedAssets.add(asset);
			}
			employee.setAssets(processedAssets);
		}
		
		employees.add(employee);
		
		return employee;
	}
	
    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                updatedEmployee.setId(id);
                employees.set(i, updatedEmployee);
                return updatedEmployee;
            }
        }
        return null;
    }
	
    @DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
    	
    	employees.removeIf(emp -> emp.getId().equals(id));
    }
}
