package com.onepieceofjava.SpringRestApiDemo.integrationTesting;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;
import com.onepieceofjava.SpringRestApiDemo.service.AssetService;
import com.onepieceofjava.SpringRestApiDemo.service.EmployeeService;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerIntegrationTesting {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private EmployeeService employeeService;
	
	@MockBean
	private AssetService assetService;
	
	private Employee testEmployee;
	
	private Asset testAsset;
	
	
	@BeforeEach
	void setUp() {
		testAsset = new Asset( 1L, "Dell Laptop", "Backend Developer Laptop","DLL12345");
		testEmployee = new Employee(1L,"Mark", "Security");
		
	}
	
	//Employees
	//Get All
	@Test
	void getGetAll_ShouldReturnEmployeesList() throws Exception {
		
		//Arrange
		List<Employee> employees = Arrays.asList(testEmployee);
		when(employeeService.getAllEmployee()).thenReturn(employees);
		
		//Act & Assert
		mockMvc.perform(get("/api/inventory/employees"))
			.andExpect(status().isOk());
		

	}
	
	//Get by Id
	
	//Add
	
	//Delete
	
	//Update
	
	//Asset
	
	//
	

}
