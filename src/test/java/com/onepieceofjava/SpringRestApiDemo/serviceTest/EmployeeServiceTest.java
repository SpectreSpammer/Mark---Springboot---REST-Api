package com.onepieceofjava.SpringRestApiDemo.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;
import com.onepieceofjava.SpringRestApiDemo.repository.EmployeeRepository;
import com.onepieceofjava.SpringRestApiDemo.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	
	@InjectMocks
	private EmployeeService employeeService;
	
	
	private Employee testEmployee;
	
	private Employee updatedEmployee;
	
	
	
	@BeforeEach
	void setUp() {
		testEmployee = new Employee(1L,"Mark", "Security");
		updatedEmployee = new Employee(1L,"Mark Anthony - Updated", "Head of Security - updated");
	}
	
	
	//Get All Employee
	@Test
	void getAllEmployee_ShouldReturnAllEmployees() {
		
		//Arrange
		List<Employee> expectedListOfEmployees = Arrays.asList(
				testEmployee,
				new Employee(2L, "Nan", "Developer")
				);
		when(employeeRepository.findAll()).thenReturn(expectedListOfEmployees);
		
		
		//Act
		List<Employee> actualEmployee = employeeService.getAllEmployee();
		
		//Assert
		assertEquals(expectedListOfEmployees.size(), actualEmployee.size());
		verify(employeeRepository).findAll();
	}
	
	//Get Employee By Id
	@Test
	void getEmployeeById_ShouldReturnEmployeeById_WhenEmployeeIsExist() {
		
		//Arrange
		
		when(employeeRepository.findById(5L)).thenReturn(Optional.of(testEmployee));
		
		//Act
		 Optional <Employee> result = employeeService.getEmployeeById(5L);
		
		//Assert
		 assertTrue(result.isPresent());
		 assertEquals(testEmployee.getName(), result.get().getName());
		   
	}
	
	
	//Add 
	@Test
	void addEmployee_ShouldReturnSavedEmployee() {
		//Arrange
		when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);
		
		//Act
		Employee savedEmployee = employeeService.addEmployee(testEmployee);
		
		//Assert
		assertNotNull(savedEmployee);
		assertEquals(testEmployee.getName(), savedEmployee.getName());
		verify(employeeRepository).save(testEmployee);
	}
	
	
	//Update employee when exist
	@Test
	void updateEmployee_ShouldReturnUpdatedEmployee_WhenEmployeeExists() {
		
		//Arrange
		when(employeeRepository.existsById(1L)).thenReturn(true);
		when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
		
		//Act
		Employee result = employeeService.updatedEmployee(1L, updatedEmployee);
		
		//Assert
		assertNotNull(result);
		assertEquals(updatedEmployee.getName(), result.getName());
		verify(employeeRepository).existsById(1L);
		verify(employeeRepository).save(updatedEmployee);
	}
	
	
	//Update employee when does not exist
	@Test
	void updateEmployee_ShouldReturnUpdatedEmployee_WhenEmployeeDoesNotExists() {
		
		//Arrange
		when(employeeRepository.existsById(1L)).thenReturn(false);
	
		
		//Act
		Employee result = employeeService.updatedEmployee(1L, updatedEmployee);
		
		//Assert
		assertNull(result);
		verify(employeeRepository).existsById(1L);
		verify(employeeRepository,never()).save(updatedEmployee);
	}
	
	//Delete
	@Test
	void deleteEmployee_ShouldDeleteTheWholeEmployee_WhenEmployeExists() {
		
		//Arrange
		Long employeeId = 1L;
		
		//Act
		employeeService.deleteEmployeeById(employeeId);
		
		//Assert
		verify(employeeRepository).deleteById(employeeId);
	}
	
	
	
	

}
