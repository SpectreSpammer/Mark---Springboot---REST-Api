package com.onepieceofjava.SpringRestApiDemo.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
		try {
			if (employee.getName() == null || employee.getName().trim().isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Employee name cannot be null");
			}

			if (employee.getDepartment() == null || employee.getDepartment().trim().isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Department cannot be null");
			}

			Employee savedEmployee = employeeService.addEmployee(employee);
			return ResponseEntity.created(URI.create("/api/inventory/employees/" + savedEmployee.getId()))
					.body(savedEmployee);
		} catch (IllegalArgumentException e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}

	//UPDATE
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
		try {
			if(updatedEmployee.getName() == null || updatedEmployee.getName().trim().isEmpty()){
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Employee name cannot be null.!");
			}

			if(updatedEmployee.getDepartment() == null || updatedEmployee.getDepartment().trim().isEmpty()){
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Department cannot be null.!");
			}



			Employee updated = employeeService.updatedEmployee(id, updatedEmployee);
			return ResponseEntity.ok(updated);

		}catch(IllegalArgumentException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(e.getMessage());
		}catch(RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}


	}

	//DELETE
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.noContent().build();
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
	public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
		Asset savedAsset = assetService.addAsset(asset);
		return ResponseEntity.created(URI.create("/api/inventory/assets/" + savedAsset.getId()))
				.body(savedAsset);
	}
	//UPDATE
	@PutMapping("/assets/{id}")
	public ResponseEntity<?> updateAsset(@PathVariable Long id, @RequestBody Asset updatedAsset) {
		try {

			//validate id
			if (updatedAsset.getId() == null) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Asset name cannot be null or empty");
			}

			// Validate name
			if (updatedAsset.getName() == null || updatedAsset.getName().trim().isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Asset name cannot be null or empty");
			}

			// Validate type
			if (updatedAsset.getType() == null || updatedAsset.getType().trim().isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Asset type cannot be null or empty");
			}

			// Validate serialNumber
			if (updatedAsset.getSerialNumber() == null || updatedAsset.getSerialNumber().trim().isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Serial number cannot be null or empty");
			}

			Asset updated = assetService.updatedAsset(id, updatedAsset);
			return ResponseEntity.ok(updated);

		} catch (IllegalArgumentException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(e.getMessage());

		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}

	//DELETE
	@DeleteMapping("/assets/{id}")
	public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
		assetService.deleteAssetById(id);
		return ResponseEntity.noContent().build();
	}


	//For assigning assets to the employee
	@PostMapping("/employees/{employeeId}/assets/{assetId}")
	public ResponseEntity<?> assignAssetToTheEmployee(@PathVariable Long employeeId, @PathVariable Long assetId) {
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

			// Save and update the employee
			Employee updatedEmployee = employeeService.addEmployee(employee);

			return ResponseEntity
					.created(URI.create("/api/inventory/employees/" + employeeId + "/assets/" + assetId))
					.body(updatedEmployee);
		}
		return ResponseEntity
				.notFound()
				.build();
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
