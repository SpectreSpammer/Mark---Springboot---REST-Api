package com.onepieceofjava.SpringRestApiDemo.integrationTesting;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
	@Test
	void getEmployeeById_ShouldReturnTheEmployeeIdDetails() throws Exception{
		
		//Arrange
		Long employeeId = 1L;
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
		
		//Act and Assert
		mockMvc.perform(get("/api/inventory/employees/{id}", employeeId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(testEmployee.getId()))
		.andExpect(jsonPath("$.name").value(testEmployee.getName()))
		.andExpect(jsonPath("$.department").value(testEmployee.getDepartment()));
		
	}
	
	//Get employee by asset
	@Test
	void getEmployeeAsset_ShouldReturnListOfAssets() throws Exception {
		
		//Arrange
		testEmployee.addAsset(testAsset);
		when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));
		
		
		//Act and Assert
		mockMvc.perform(get("/api/inventory/employees/1/assets"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].name").value(testAsset.getName()));
	}
	
	//Add
    @Test
    void addEmployee_ShouldReturnCreatedEmployee() throws Exception {
        // Arrange
        Employee newEmployee = new Employee(null, "Mark", "HR");
        when(employeeService.addEmployee(any(Employee.class)))
            .thenReturn(new Employee(2L, "Mark", "HR"));

        // Act & Assert
        mockMvc.perform(post("/api/inventory/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Mark"))
                .andExpect(jsonPath("$.department").value("HR"))
                .andExpect(jsonPath("$.assets").isArray())
                .andExpect(jsonPath("$.assets").isEmpty());

        verify(employeeService, times(1)).addEmployee(any(Employee.class));
    }
	
    //Add null values
    @Test
    void addEmployeeWithNullName_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Employee invalidEmployee = new Employee(null, null, "HR");

        // Act & Assert
        mockMvc.perform(post("/api/inventory/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).addEmployee(any(Employee.class));
    }
    
    //Add existing id
    @Test
    void addEmployeeWIthExistingId_ShouldIgnoreId() throws Exception{
    	
    	//Arrange
    	Employee employeeWithId = new Employee(10L,"Nan","Sales");
    	Employee savedEmployee = new Employee(2L,"Nan","Sales");
    	when(employeeService.addEmployee(any(Employee.class))).thenReturn(savedEmployee);
    	
    	
    	//Act and Assert
    	mockMvc.perform(post("/api/inventory/employees")
    	.contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employeeWithId)))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.id").value(2L))
        .andExpect(jsonPath("$.name").value("Nan"))
        .andExpect(jsonPath("$.department").value("Sales"));
    	
    	  verify(employeeService, times(1)).addEmployee(any(Employee.class));
    }
	//Delete
	
	//Update
	
	//Asset
	
	//
	

}
