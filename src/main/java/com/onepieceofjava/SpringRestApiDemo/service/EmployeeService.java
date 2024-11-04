package com.onepieceofjava.SpringRestApiDemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;
import com.onepieceofjava.SpringRestApiDemo.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	
	@Autowired
	private EmployeeRepository employeeRepository;

	
	//getAll
	public List<Employee> getAllEmployee(){
		return employeeRepository.findAll();
	}
		
	//get by id
	public Optional<Employee> getEmployeeById(Long id){
			
		return employeeRepository.findById(id);
	}
		
		
	//add 
	public Employee addEmployee(Employee employee) {
			return employeeRepository.save(employee);
	}
		
	//update
	public Employee updatedEmployee(Long id, Employee updatedEmployee) {
		if(employeeRepository.existsById(id)) {
			updatedEmployee.setId(id);
			return  employeeRepository.save(updatedEmployee);
		}
			return null;
	}
		
	//delete
	public void deleteEmployeeById(Long id) {
		employeeRepository.deleteById(id);
	}
}
