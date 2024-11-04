package com.onepieceofjava.SpringRestApiDemo.oldCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class OldEmployeeService {
	
	List<OldEmployee> employees = new ArrayList<>();
	
	private Long employeeId = 101L;
	
	//GET
	public List<OldEmployee> getAllEmployees(){
		return employees;
	}
	
	//GET BY ID
	public Optional<OldEmployee> getEmployeeById(Long id){
		return employees.stream().filter(asset -> asset.getId().equals(id)).findFirst();
	}
	
	//ADD
	public OldEmployee addEmployee(OldEmployee employee) {
		employee.setId(employeeId++);
		employees.add(employee);
		
		return employee;
	}
	
	//UPDATE
	public OldEmployee updateEmployee(Long id, OldEmployee updatedEmployee) {
		
		Optional<OldEmployee> employeeOptional = getEmployeeById(id);
		if(employeeOptional.isPresent()) {
			updatedEmployee.setId(id);
			employees.set(employees.indexOf(employeeOptional.get()), updatedEmployee);
			return updatedEmployee;
		}
		
		return null;
	}
	
	//DELETE
	public void deleteEmployeeById(Long id) {
		employees.removeIf(emp -> emp.getId().equals(id));
	}

}
