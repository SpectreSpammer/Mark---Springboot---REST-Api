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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;





import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;


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
    @Test
    void deleteEmployee_ShouldDeleteEmployee() throws Exception{
    	
    	//Arrange
    	Long employeeId = 1L;
    	Employee deleteExistingEmployee = new Employee(employeeId,"Nan","Sales");
    	when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    	doNothing().when(employeeService).deleteEmployeeById(employeeId);
    	
    	//Act and Assert
    	mockMvc.perform(delete("/api/inventory/employees/{id}",employeeId))
    	.andExpect(status().isNoContent());
    	
    	verify(employeeService,times(1)).deleteEmployeeById(employeeId);
    }
	
	//Update
    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception{
    	
    	//Arrange
    	Long employeeId = 1L;
    	Employee updateEmployee = new Employee(null,"Nan","Sales");
    	Employee savedEmployee = new Employee(employeeId,"Nan","Sales");
    	when(employeeService.updatedEmployee(eq(employeeId), any(Employee.class)))
    	.thenReturn(savedEmployee);
    	
    	
    	//Act and Assert
    	mockMvc.perform(put("/api/inventory/employees/{id}",employeeId)
    	    	.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(updateEmployee)))
    	    	.andExpect(status().isOk())
    	    	.andExpect(jsonPath("$.id").value(employeeId))
    	        .andExpect(jsonPath("$.name").value("Nan"))
    	        .andExpect(jsonPath("$.department").value("Sales"))
		        .andExpect(jsonPath("$.assets").isArray())
		        .andExpect(jsonPath("$.assets").isEmpty());
    	
    	verify(employeeService, times(1)).updatedEmployee(eq(employeeId), any(Employee.class));
    }
    
    @Test
    void updateEmployee_WithNullName_ShouldReturnBadRequest() throws Exception{
    	
    	//Arrange
    	Long employeeId = 1L;
    	Employee invalidEmployee = new Employee(null,null,"Sales");
    	
    	
    	//Act and assert
    	mockMvc.perform(put("/api/inventory/employees/{id}",employeeId)
    	    	.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(invalidEmployee)))
    	    	.andExpect(status().isBadRequest());
    	    	
    	verify(employeeService, never()).updatedEmployee(any(Long.class), any(Employee.class));
    }
    
    @Test
    void updateEmployee_WithNullDepartment_ShouldReturnBadRequest() throws Exception{
    	
    	//Arrange
    	Long employeeId = 1L;
    	Employee invalidEmployee = new Employee(null,"Nan",null);
    	
    	
    	//Act and assert
    	mockMvc.perform(put("/api/inventory/employees/{id}",employeeId)
    	    	.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(invalidEmployee)))
    	    	.andExpect(status().isBadRequest());
    	    	
    	verify(employeeService, never()).updatedEmployee(any(Long.class), any(Employee.class));
    }
    
    
    @Test
    void updateEmployee_WithInvalidId_ShouldReturnBadRequest()throws Exception {
    	//Arrange
    	String invalidEmployeeId = "12345ASD";
    
    	
    	
    	//Act and assert
    	mockMvc.perform(put("/api/inventory/employees/{id}",invalidEmployeeId)
    	    	.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(testEmployee)))
    	    	.andExpect(status().isBadRequest());
    	    	
    	verify(employeeService, never()).updatedEmployee(anyLong(), any(Employee.class));
    }
    
    @Test
    void updateEmployee_WhenEmployeeNotFound_ShouldReturnNotFound() throws Exception {
    	
    	//Arrange
    	Long employeeId = 999L;
    	Employee updateEmployee = new Employee(null,"Nan","Sales");
    	
    	when(employeeService.updatedEmployee(eq(employeeId), any(Employee.class)))
    	.thenThrow(new IllegalArgumentException("Employee with id " + employeeId + "not found.!"));
    	
    	//Act and Assert
    	mockMvc.perform(put("/api/inventory/employees/{id}",employeeId)
    	    	.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(updateEmployee)))
    	    	.andExpect(status().isNotFound());
    	
    	verify(employeeService, times(1)).updatedEmployee(eq(employeeId), any(Employee.class));
    }
    
	
	//Asset
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
	
	//GET By Id
	@Test
	void getAssetById_ShouldReturnAssets()  throws Exception{
		
		//Arrange
		Long assetId = 201L;
		Asset testAsset = new Asset(assetId,"Lenovo","Developer Laptop","LNV10123");
		
		when(assetService.getAssetById(assetId)).thenReturn(Optional.of(testAsset));
		
	
		//Act and Assert
		mockMvc.perform(get("/api/inventory/assets/{id}",assetId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(assetId))
        .andExpect(jsonPath("$.name").value("Lenovo"))
        .andExpect(jsonPath("$.type").value("Developer Laptop"))
        .andExpect(jsonPath("$.serialNumber").value("LNV10123"));
		
		verify(assetService,times(1)).getAssetById(assetId);
        
	}
	//Add
	@Test
	void addAsset_ShouldReturnAddAsset()  throws Exception{
		
		//Arrange
		Asset newAsset = new Asset(null,"Lenovo","Developer Laptop","LNV10123");
		Asset savedAsset = new Asset(202L,"Lenovo","Developer Laptop","LNV10123");
		
		when(assetService.addAsset(any(Asset.class))).thenReturn(savedAsset);
		
		//Act and  Assert
		mockMvc.perform(post("/api/inventory/assets")
				.contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(newAsset)))	
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(savedAsset.getId()))
		        .andExpect(jsonPath("$.name").value("Lenovo"))
		        .andExpect(jsonPath("$.type").value("Developer Laptop"))
		        .andExpect(jsonPath("$.serialNumber").value("LNV10123"));
		
		verify(assetService,times(1)).addAsset(any(Asset.class));
	}

	//Update -> validate invalid id ( 123abc )
	@Test
	void updateAsset_WithInvalidId_ShouldReturnBadRequest() throws Exception{
		//Arrange
		String invalidAssetId = "123abc";

		//Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}",invalidAssetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService,never()).updatedAsset(any(Long.class),any(Asset.class));
	}


	//Update -> validate null asset id
	/*
	@Test
	void updateAsset_withNullAssetId_ShouldReturnBadRequest() throws Exception{
		//Arrange
		Long assetId = 2L;
		Asset invalidAsset =  new Asset(null,"Lenovo","HR Laptop","abc123");

		//Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}",assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService,never()).updatedAsset(any(Long.class),any(Asset.class));

	}
	*/

	//Update -> validate null asset name
	@Test
	void updateAsset_withNullAssetName_ShouldReturnBadRequest() throws Exception{
		//Arrange
		Long assetId = 2L;
		Asset invalidAsset =  new Asset(null,null,"HR Laptop","abc123");

		//Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}",assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
						.andExpect(status().isBadRequest());

		verify(assetService,never()).updatedAsset(any(Long.class),any(Asset.class));
	}

	//Update -> validate null asset type
	@Test
	void updateAsset_withNullAssetType_ShouldReturnBadRequest() throws Exception{
		//Arrange
		Long assetId = 2L;
		Asset invalidAsset =  new Asset(null,"Lenovo",null,"abc123");

		//Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}",assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService,never()).updatedAsset(any(Long.class),any(Asset.class));
	}

	//Update -> validate null serial number
	@Test
	void updateAsset_withNullSerialNumber_ShouldReturnBadRequest() throws Exception{
		//Arrange
		Long assetId = 2L;
		Asset invalidAsset =  new Asset(null,"Lenovo","HR",null);

		//Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}",assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService,never()).updatedAsset(any(Long.class),any(Asset.class));
	}

	//Update -> asset when not found
	/*
	@Test
	void updateAsset_WhenAssetNotFound_ShouldReturnNotFound() throws Exception {
		// Arrange
		Long assetId = 999L;
		Asset updatedAsset = new Asset(null, "Dell", "HR laptop", "SN123");

		when(assetService.updatedAsset(eq(assetId), any(Asset.class)))
				.thenThrow(new IllegalArgumentException("Asset with id " + assetId + " not found!"));

		// Act and Assert
		mockMvc.perform(put("/api/inventory/assets/{id}", assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedAsset)))
				.andExpect(status().isNotFound());

		verify(assetService, times(1)).updatedAsset(eq(assetId), any(Asset.class));
	}
	*/


	//Delete
	@Test
	void deleteAsset_ShouldReturnNoContent() throws  Exception {
		//Arrange
		Long assetId = 1L;
		doNothing().when(assetService).deleteAssetById(assetId);

		//Act and Assert
		mockMvc.perform(delete("/api/inventory/assets/{id}",assetId))
					.andExpect(status().isNoContent())
					.andExpect(content().string(""));


		verify(assetService,times(1)).deleteAssetById(assetId);
	}
	
	

}
