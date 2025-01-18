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


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


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
@AutoConfigureMockMvc(addFilters = false)
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

	// Employee Tests
	@Test
	void getAllEmployees_ShouldReturnEmployeesList() throws Exception {
		// Arrange
		List<Employee> employees = Arrays.asList(testEmployee);
		when(employeeService.getAllEmployee()).thenReturn(employees);

		// Act & Assert
		mockMvc.perform(get("/api/inventory/employees")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(testEmployee.getId()))
				.andExpect(jsonPath("$[0].name").value(testEmployee.getName()))
				.andExpect(jsonPath("$[0].department").value(testEmployee.getDepartment()));
	}

	@Test
	void getEmployeeById_ShouldReturnEmployee() throws Exception {
		// Arrange
		when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

		// Act & Assert
		mockMvc.perform(get("/api/inventory/employees/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testEmployee.getId()))
				.andExpect(jsonPath("$.name").value(testEmployee.getName()))
				.andExpect(jsonPath("$.department").value(testEmployee.getDepartment()));
	}

	@Test
	void addEmployee_ShouldReturnCreatedEmployee() throws Exception {
		// Arrange
		Employee newEmployee = new Employee(null, "Mark", "HR");
		Employee savedEmployee = new Employee(2L, "Mark", "HR");
		when(employeeService.addEmployee(any(Employee.class))).thenReturn(savedEmployee);

		// Act & Assert
		mockMvc.perform(post("/api/inventory/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newEmployee)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/inventory/employees/2"))
				.andExpect(jsonPath("$.id").value(2L))
				.andExpect(jsonPath("$.name").value("Mark"))
				.andExpect(jsonPath("$.department").value("HR"))
				.andExpect(jsonPath("$.assets").isArray())
				.andExpect(jsonPath("$.assets").isEmpty());

		verify(employeeService, times(1)).addEmployee(any(Employee.class));
	}

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

	@Test
	void addEmployeeWithExistingId_ShouldIgnoreId() throws Exception {
		// Arrange
		Employee employeeWithId = new Employee(10L, "Nan", "Sales");
		Employee savedEmployee = new Employee(2L, "Nan", "Sales");
		when(employeeService.addEmployee(any(Employee.class))).thenReturn(savedEmployee);

		// Act & Assert
		mockMvc.perform(post("/api/inventory/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employeeWithId)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(2L))
				.andExpect(jsonPath("$.name").value("Nan"))
				.andExpect(jsonPath("$.department").value("Sales"));

		verify(employeeService, times(1)).addEmployee(any(Employee.class));
	}

	@Test
	void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
		// Arrange
		Long employeeId = 1L;
		Employee updateEmployee = new Employee(null, "Nan", "Sales");
		Employee savedEmployee = new Employee(employeeId, "Nan", "Sales");
		when(employeeService.updatedEmployee(eq(employeeId), any(Employee.class)))
				.thenReturn(savedEmployee);

		// Act & Assert
		mockMvc.perform(put("/api/inventory/employees/{id}", employeeId)
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
	void updateEmployeeWithNullName_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Long employeeId = 1L;
		Employee invalidEmployee = new Employee(null, null, "Sales");

		// Act & Assert
		mockMvc.perform(put("/api/inventory/employees/{id}", employeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidEmployee)))
				.andExpect(status().isBadRequest());

		verify(employeeService, never()).updatedEmployee(any(Long.class), any(Employee.class));
	}

	@Test
	void updateEmployeeWithNullDepartment_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Long employeeId = 1L;
		Employee invalidEmployee = new Employee(null, "Nan", null);

		// Act & Assert
		mockMvc.perform(put("/api/inventory/employees/{id}", employeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidEmployee)))
				.andExpect(status().isBadRequest());

		verify(employeeService, never()).updatedEmployee(any(Long.class), any(Employee.class));
	}

	@Test
	void updateEmployeeWithInvalidId_ShouldReturnBadRequest() throws Exception {
		// Arrange
		String invalidEmployeeId = "12345ASD";

		// Act & Assert
		mockMvc.perform(put("/api/inventory/employees/{id}", invalidEmployeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testEmployee)))
				.andExpect(status().isBadRequest());

		verify(employeeService, never()).updatedEmployee(anyLong(), any(Employee.class));
	}

	@Test
	void updateEmployeeWhenNotFound_ShouldReturnNotFound() throws Exception {
		// Arrange
		Long employeeId = 999L;
		Employee updateEmployee = new Employee(null, "Nan", "Sales");
		when(employeeService.updatedEmployee(eq(employeeId), any(Employee.class)))
				.thenThrow(new IllegalArgumentException("Employee with id " + employeeId + " not found!"));

		// Act & Assert
		mockMvc.perform(put("/api/inventory/employees/{id}", employeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateEmployee)))
				.andExpect(status().isNotFound());

		verify(employeeService, times(1)).updatedEmployee(eq(employeeId), any(Employee.class));
	}

	@Test
	void deleteEmployee_ShouldReturnNoContent() throws Exception {
		// Arrange
		Long employeeId = 1L;
		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
		doNothing().when(employeeService).deleteEmployeeById(employeeId);

		// Act & Assert
		mockMvc.perform(delete("/api/inventory/employees/{id}", employeeId))
				.andExpect(status().isNoContent());

		verify(employeeService, times(1)).deleteEmployeeById(employeeId);
	}

	// Asset Tests
	@Test
	void getEmployeeAssets_ShouldReturnListOfAssets() throws Exception {
		// Arrange
		testEmployee.addAsset(testAsset);
		when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

		// Act & Assert
		mockMvc.perform(get("/api/inventory/employees/1/assets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name").value(testAsset.getName()));
	}

	@Test
	void getAssetById_ShouldReturnAsset() throws Exception {
		// Arrange
		when(assetService.getAssetById(1L)).thenReturn(Optional.of(testAsset));

		// Act & Assert
		mockMvc.perform(get("/api/inventory/assets/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(testAsset.getId()))
				.andExpect(jsonPath("$.name").value(testAsset.getName()))
				.andExpect(jsonPath("$.type").value(testAsset.getType()))
				.andExpect(jsonPath("$.serialNumber").value(testAsset.getSerialNumber()));
	}

	@Test
	void addAsset_ShouldReturnCreatedAsset() throws Exception {
		// Arrange
		Asset newAsset = new Asset(null, "Lenovo", "Developer Laptop", "LNV10123");
		Asset savedAsset = new Asset(202L, "Lenovo", "Developer Laptop", "LNV10123");
		when(assetService.addAsset(any(Asset.class))).thenReturn(savedAsset);

		// Act & Assert
		mockMvc.perform(post("/api/inventory/assets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAsset)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/inventory/assets/202"))
				.andExpect(jsonPath("$.id").value(savedAsset.getId()))
				.andExpect(jsonPath("$.name").value("Lenovo"))
				.andExpect(jsonPath("$.type").value("Developer Laptop"))
				.andExpect(jsonPath("$.serialNumber").value("LNV10123"));

		verify(assetService, times(1)).addAsset(any(Asset.class));
	}

	@Test
	void updateAssetWithInvalidId_ShouldReturnBadRequest() throws Exception {
		// Arrange
		String invalidAssetId = "123abc";

		// Act & Assert
		mockMvc.perform(put("/api/inventory/assets/{id}", invalidAssetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService, never()).updatedAsset(any(Long.class), any(Asset.class));
	}

	@Test
	void updateAssetWithNullId_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Asset validAsset = new Asset(null, "Lenovo", "HR Laptop", "abc123");

		// Act & Assert
		mockMvc.perform(put("/api/inventory/assets/null")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(validAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService, never()).updatedAsset(any(Long.class), any(Asset.class));
	}

	@Test
	void updateAssetWithNullName_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Long assetId = 2L;
		Asset invalidAsset = new Asset(null, null, "HR Laptop", "abc123");

		// Act & Assert
		mockMvc.perform(put("/api/inventory/assets/{id}", assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService, never()).updatedAsset(any(Long.class), any(Asset.class));
	}

	@Test
	void updateAssetWithNullType_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Long assetId = 2L;
		Asset invalidAsset = new Asset(null, "Lenovo", null, "abc123");

		// Act & Assert
		mockMvc.perform(put("/api/inventory/assets/{id}", assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService, never()).updatedAsset(any(Long.class), any(Asset.class));
	}

	@Test
	void updateAssetWithNullSerialNumber_ShouldReturnBadRequest() throws Exception {
		// Arrange
		Long assetId = 2L;
		Asset invalidAsset = new Asset(null, "Lenovo", "HR", null);

		// Act & Assert
		mockMvc.perform(put("/api/inventory/assets/{id}", assetId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidAsset)))
				.andExpect(status().isBadRequest());

		verify(assetService, never()).updatedAsset(any(Long.class), any(Asset.class));
	}

	@Test
	void assignAssetToEmployee_ShouldReturnCreated() throws Exception {
		// Arrange
		Long employeeId = 1L;
		Long assetId = 1L;

		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
		when(assetService.getAssetById(assetId)).thenReturn(Optional.of(testAsset));
		when(assetService.addAsset(any(Asset.class))).thenReturn(testAsset);
		when(employeeService.addEmployee(any(Employee.class))).thenReturn(testEmployee);

		// Act & Assert
		mockMvc.perform(post("/api/inventory/employees/{employeeId}/assets/{assetId}", employeeId, assetId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/inventory/employees/1/assets/1"))
				.andExpect(jsonPath("$.id").value(testEmployee.getId()))
				.andExpect(jsonPath("$.name").value(testEmployee.getName()))
				.andExpect(jsonPath("$.department").value(testEmployee.getDepartment()));

		verify(employeeService, times(1)).getEmployeeById(employeeId);
		verify(assetService, times(1)).getAssetById(assetId);
		verify(assetService, times(1)).addAsset(any(Asset.class));
		verify(employeeService, times(1)).addEmployee(any(Employee.class));
	}

	@Test
	void assignAssetToEmployee_WhenEmployeeNotFound_ShouldReturnNotFound() throws Exception {
		// Arrange
		Long employeeId = 999L;
		Long assetId = 1L;

		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
		when(assetService.getAssetById(assetId)).thenReturn(Optional.of(testAsset));

		// Act & Assert
		mockMvc.perform(post("/api/inventory/employees/{employeeId}/assets/{assetId}", employeeId, assetId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(employeeService, times(1)).getEmployeeById(employeeId);
		verify(assetService, times(1)).getAssetById(assetId);
		verify(assetService, never()).addAsset(any(Asset.class));
		verify(employeeService, never()).addEmployee(any(Employee.class));
	}

	@Test
	void assignAssetToEmployee_WhenAssetNotFound_ShouldReturnNotFound() throws Exception {
		// Arrange
		Long employeeId = 1L;
		Long assetId = 999L;

		when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
		when(assetService.getAssetById(assetId)).thenReturn(Optional.empty());

		// Act & Assert
		mockMvc.perform(post("/api/inventory/employees/{employeeId}/assets/{assetId}", employeeId, assetId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(employeeService, times(1)).getEmployeeById(employeeId);
		verify(assetService, times(1)).getAssetById(assetId);
		verify(assetService, never()).addAsset(any(Asset.class));
		verify(employeeService, never()).addEmployee(any(Employee.class));
	}

	@Test
	void deleteAsset_ShouldReturnNoContent() throws Exception {
		// Arrange
		Long assetId = 1L;
		doNothing().when(assetService).deleteAssetById(assetId);

		// Act & Assert
		mockMvc.perform(delete("/api/inventory/assets/{id}", assetId))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(assetService, times(1)).deleteAssetById(assetId);
	}
}
