package com.onepieceofjava.SpringRestApiDemo.integrationTesting;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;
import com.onepieceofjava.SpringRestApiDemo.service.AssetService;
import com.onepieceofjava.SpringRestApiDemo.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

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


    //CRUD
}
