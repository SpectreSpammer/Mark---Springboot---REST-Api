package com.onepieceofjava.SpringRestApiDemo.oldCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/old/inventory")
public class OldInventoryController {
	
	private List<OldEmployee> employees = new ArrayList<>();
	
	private List<OldAsset> assets = new ArrayList<>();
	
	private Long employeeId = 101L;
	
	private Long assetId = 201L;
	
	//For employees
	//get All
	@GetMapping("employees")
	public List<OldEmployee> getAllEmployees(){
		return employees;
	}
	
	//get by Id
	@GetMapping("employees/{id}")
	public OldEmployee getEmployeeById(@PathVariable Long id) {
		return employees.stream()
				.filter(emp -> emp.getId().equals(id))
				.findFirst()
				.orElse(null);
	}
	
	//Adding of employees
	
	@PostMapping("/employees")
	public OldEmployee addEmployee(@RequestBody OldEmployee employee) {
		
		employee.setId(employeeId++);
		
		if(employee.getAssets() != null && !employee.getAssets().isEmpty()) {
			List<OldAsset> processedAssets = new ArrayList<>();
			for( OldAsset asset : employee.getAssets()) {
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
    public OldEmployee updateEmployee(@PathVariable Long id, @RequestBody OldEmployee updatedEmployee) {
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
    
    //For assets
    //Get all Assets
    //http://localhost:9095/api/inventory/assets
	@GetMapping("/assets")
	public List<OldAsset> getAllAssets(){
		return assets;
	}
	
	//Add Assets
	//http://localhost:9095/api/inventory/assets
	@PostMapping("/assets")
	public OldAsset addAsset(@RequestBody OldAsset asset) {
		asset.setId(assetId++);
		assets.add(asset);
		
		return asset;
	}
	
	
	//Get asset by specific employee
	//http://localhost:9095/api/inventory/employees/101/assets
	@GetMapping("/employees/{employeeId}/assets")
	public List<OldAsset> getEmployeeAssetsByEmployeeId(@PathVariable Long employeeId){
		
		Optional<OldEmployee> emp = employees.stream()
				.filter(empl -> empl.getId()
				.equals(employeeId)).findFirst();

		
		return emp.map(OldEmployee::getAssets).orElse(null);
	}
	
	//Add asset by specific employee
	//http://localhost:9095/api/inventory/employees/102/assets/202
	@PostMapping("/employees/{employeeId}/assets/{assetId}")
	public OldEmployee assignAssetToSpecificEmployee(@PathVariable Long employeeId,@PathVariable Long assetId) {
		
		Optional<OldEmployee> emp = employees.stream()
								.filter(empl -> empl.getId()
								.equals(employeeId)).findFirst();
		
		Optional<OldAsset> ast = assets.stream()
				.filter(asts -> asts.getId()
				.equals(assetId)).findFirst();
		
		if(emp.isPresent() && ast.isPresent()) {
			OldEmployee employee = emp.get();
			OldAsset asset = ast.get();
			employee.addAsset(asset);
			return employee;
		}
		return null;
	}
	
	
	//Delete specific asset from the employee
	//http:localhost:8080/api/inventory/employees/101/assets/201
	@DeleteMapping("/employees/{employeeId}/assets/{assetId}")
	public OldEmployee removeAssetFromEmployee(@PathVariable Long employeeId, @PathVariable Long assetId) {
		
		Optional<OldEmployee> emp = employees.stream()
				.filter(empl -> empl.getId()
				.equals(employeeId)).findFirst();

		Optional<OldAsset> ast = assets.stream()
				.filter(asts -> asts.getId()
				.equals(assetId)).findFirst();
		
		
		if(emp.isPresent() && ast.isPresent()) {
			OldEmployee employee = emp.get();
			OldAsset asset = ast.get();
			employee.removeAsset(asset);
			return employee;
		}
		return null;
	}
	
	//Delete all assets
	@DeleteMapping("/assets/{id}")
	public void deleteAsset(@PathVariable Long id) {
	    	
	    assets.removeIf(ast -> ast.getId().equals(id));
	}
	    
	
	
	
	
}
