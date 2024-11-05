package com.onepieceofjava.SpringRestApiDemo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.onepieceofjava.SpringRestApiDemo.oldCodes.OldAsset;
import com.onepieceofjava.SpringRestApiDemo.oldCodes.OldAssetService;
import com.onepieceofjava.SpringRestApiDemo.oldCodes.OldEmployee;
import com.onepieceofjava.SpringRestApiDemo.oldCodes.OldEmployeeService;
import com.onepieceofjava.SpringRestApiDemo.service.AssetService;
import com.onepieceofjava.SpringRestApiDemo.service.EmployeeService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

	@Autowired
	AssetService assetService;
	
	@Autowired
	EmployeeService employeeService;
	
	
	
	
	//For Employees
	//GET ALL
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return employeeService.getAllEmployee();
	}
	
	//GET by Id
	@GetMapping("/employees/{id}")
	public Employee getEmployeeById(@PathVariable Long id) {
		return employeeService.getEmployeeById(id).orElse(null);
		
	}
	
	//ADD
	@PostMapping("/employees")
	public Employee addEmployee(@RequestBody Employee employee) {
		return employeeService.addEmployee(employee);
	}
	
	//UPDATE
	@PutMapping("/employees/{id}")
	public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
		return employeeService.updatedEmployee(id, updatedEmployee);
	}
	
	//DELETE
	@DeleteMapping("/employees/{id}")
	public void deleteEmployeeById(@PathVariable Long id) {
		employeeService.deleteEmployeeById(id);
	}
	
	
	//For Assets
	//GET All
	@GetMapping("/assets")
	public List<Asset> getAllAssets(){
		return assetService.getAllAsset();
	}
	
	//GET Asset by Id
	@GetMapping("/assets/{id}")
	public Asset getAssetsyId(@PathVariable Long id) {
		return assetService.getAssetById(id).orElse(null);
		
	}
	
	//ADD 
	@PostMapping("/assets")
	public Asset addAsset(@RequestBody Asset asset) {
		return assetService.addAsset(asset);
	}
	
	//UPDATE
	@PutMapping("/assets/{id}")
	public Asset updateAsset(@PathVariable Long id, @RequestBody Asset updatedAsset) {
		return assetService.updatedAsset(id, updatedAsset);
	}
	
	//DELETE
	@DeleteMapping("/assets/{id}")
	public void deleteAsset(@PathVariable Long id) {
		assetService.deleteAssetById(id);
	}
	
	
	//For assigning assets to the employee
	@PostMapping("/employees/{employeeId}/assets/{assetId}")
	public Employee assignAssetToTheEmployee(@PathVariable Long employeeId, @PathVariable Long assetId) {
	    Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);
	    Optional<Asset> assetOptional = assetService.getAssetById(assetId);
	    
	    if(employeeOptional.isPresent() && assetOptional.isPresent()) {
	        Employee employee = employeeOptional.get();
	        Asset asset = assetOptional.get();
	        
	        // Set the relationship on both sides
	        asset.setEmployee(employee);
	        employee.addAsset(asset);
	        
	        // Save the updated asset
	        assetService.addAsset(asset);
	        
	        // Save and return the updated employee
	        return employeeService.addEmployee(employee);
	    }
	    return null;
	}
	
	
	@DeleteMapping("/employees/{employeeId}/assets/{assetId}")
	public Employee deleteAssetToTheEmployee(@PathVariable Long employeeId,@PathVariable Long assetId) {
		
		Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);
		Optional<Asset> assetOptional = assetService.getAssetById(assetId);
		
		
		if(employeeOptional.isPresent() && assetOptional.isPresent()) {
			Employee employee = employeeOptional.get();
			employee.removeAsset(assetOptional.get());
			return employee;
		}
		return null;
	}
	
	
	@GetMapping("/employees/{employeeId}/assets")
	public List<Asset> getEmployeeAssetsById(@PathVariable Long employeeId) {
		Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);
		return employeeOptional.map(Employee::getAssets).orElse(null);
	}
		
		
	
}
