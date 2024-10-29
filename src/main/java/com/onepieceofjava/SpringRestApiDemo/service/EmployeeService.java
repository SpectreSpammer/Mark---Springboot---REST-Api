package com.onepieceofjava.SpringRestApiDemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onepieceofjava.SpringRestApiDemo.model.Employee;

@Service
public class EmployeeService {
	
	List<Employee> employees = new ArrayList<>();
	
	private Long employeeId = 101L;
	
	//GET
	public List<Employee> getAllEmployees(){
		return employees;
	}
	
	//GET BY ID
	public Optional<Employee> getEmployeeById(Long id){
		return employees.stream().filter(asset -> asset.getId().equals(id)).findFirst();
	}
	
	//ADD
	public Employee addEmployee(Employee employee) {
		employee.setId(employeeId++);
		employees.add(employee);
		
		return employee;
	}
	
	//UPDATE
	public Employee updateEmployee(Long id, Employee updatedEmployee) {
		
		Optional<Employee> employeeOptional = getEmployeeById(id);
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
