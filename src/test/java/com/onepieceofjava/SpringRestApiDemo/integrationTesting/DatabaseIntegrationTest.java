package com.onepieceofjava.SpringRestApiDemo.integrationTesting;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;
import com.onepieceofjava.SpringRestApiDemo.service.AssetService;
import com.onepieceofjava.SpringRestApiDemo.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class DatabaseIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AssetService assetService;

    private Employee testEmployee;

    private Asset testAsset;

    @BeforeEach()
    void setUp(){
        //test data
        testEmployee = new Employee(null,"Mark","IT");
        testAsset = new Asset(null,"Lenovo", "Developer Laptop","LNV123");

    }

    //Employee
    //Create
    @Test
    void shouldSaveEmployeeAndRetrieve(){
        //save
        Employee savedEmployee = employeeService.addEmployee(testEmployee);
        assertNotNull(savedEmployee.getId());

        //retrieved
        Optional<Employee> retrievedEmployee = employeeService.getEmployeeById(savedEmployee.getId());
        assertTrue(retrievedEmployee.isPresent());
        assertEquals("Mark",retrievedEmployee.get().getName());
        assertEquals("IT",retrievedEmployee.get().getDepartment());
    }

    //READ
    @Test
    void shouldGetAllEmployees(){
        //saved
        employeeService.addEmployee(testEmployee);
        employeeService.addEmployee(new Employee( null, "Nan", " Backend Developer"));

        //retrieved
        List<Employee> employees = employeeService.getAllEmployee();
        assertFalse(employees.isEmpty());
        assertTrue(employees.size() >= 2);
    }
    //update
    @Test
    void shouldUpdateEmployee(){
        //save
        Employee savedEmployee = employeeService.addEmployee(testEmployee);

        //update
        savedEmployee.setDepartment("PR");
        Employee updatedEmployee = employeeService.updatedEmployee(savedEmployee.getId(),savedEmployee);

        //verify
        assertEquals("PR", updatedEmployee.getDepartment());
        Optional<Employee>  retrievedEmployee = employeeService.getEmployeeById(savedEmployee.getId());
        assertTrue(retrievedEmployee.isPresent());
        assertEquals("PR", retrievedEmployee.get().getDepartment());
    }

    //delete
    @Test
    void shouldDeleteEmployeesById(){
        //saved
        Employee savedEmployee = employeeService.addEmployee(testEmployee);
        assertNotNull(savedEmployee.getId(),"Employees should have been saved with an Id");

        //delete
        employeeService.deleteEmployeeById(savedEmployee.getId());

        //verify
        Optional<Employee> retrievedEmployee = employeeService.getEmployeeById(savedEmployee.getId());
        assertFalse(retrievedEmployee.isPresent(),"Employee should not exist after deletion");
    }

    //Asset
    //Create
    @Test
    void shouldSavedAndRetrievedAsset(){
        //save
        Asset savedAsset = assetService.addAsset(testAsset);
        assertNotNull(savedAsset.getId());

        //retrieved
        Optional<Asset> retrievedAsset = assetService.getAssetById(savedAsset.getId());
        assertTrue(retrievedAsset.isPresent());
        assertEquals("Lenovo",retrievedAsset.get().getName());
        assertEquals("Developer Laptop",retrievedAsset.get().getType());
        assertEquals("LNV123",retrievedAsset.get().getSerialNumber());
    }

    //read
    @Test
    void shouldGetAllAsset(){
        //saved
        assetService.addAsset(testAsset);
        assetService.addAsset(new Asset(null,"Acer","HR Laptop","ACR123"));


        //retrieved
        List<Asset> assets = assetService.getAllAsset();
        assertFalse(assets.isEmpty());
        assertTrue(assets.size() >= 2);
    }
    //update
    @Test
    void shouldUpdateAsset(){
        //saved
        Asset savedAsset = assetService.addAsset(testAsset);

        //update
        savedAsset.setName("Dell");
        Asset updatedAsset = assetService.updatedAsset(savedAsset.getId(), savedAsset);

        //verify
        assertEquals("Dell",updatedAsset.getName());
        Optional<Asset> retrievedAsset = assetService.getAssetById(savedAsset.getId());
        assertTrue(retrievedAsset.isPresent());
        assertEquals("Dell", retrievedAsset.get().getName());
    }


    //delete
    @Test
    void shouldDeleteAssetById(){
        //saved
        Asset savedAsset = assetService.addAsset(testAsset);

        //delete
        assetService.deleteAssetById(savedAsset.getId());

        //verify
        Optional<Asset> retrievedAsset = assetService.getAssetById(savedAsset.getId());
        assertFalse(retrievedAsset.isPresent());
    }

    //CRUD
}
