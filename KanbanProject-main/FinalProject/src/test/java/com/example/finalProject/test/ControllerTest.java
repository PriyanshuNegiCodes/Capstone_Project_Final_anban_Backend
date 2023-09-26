package com.example.finalProject.test;

import com.example.finalProject.controller.EmployeeController;
import com.example.finalProject.domain.Employee;
import com.example.finalProject.domain.EmployeeDTO;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;
import com.example.finalProject.services.IEmployeeServices;
import com.example.finalProject.services.ISecurityTokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
    @Mock
    private IEmployeeServices employeeServices;
    @Mock
    private ISecurityTokenGenerator securityTokenGenerator;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addNewUserSuccess() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("Priyanshu", "password");
        Employee employee = new Employee(employeeDTO.getUserName(), employeeDTO.getPassword());
        when(employeeServices.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/v1/auth/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employeeDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void loginCustomerSuccess() throws Exception {
        Employee employee = new Employee("Priyanshu", "password");
        // Mock the behavior of the employeeServices to return a valid Employee
        when(employeeServices.getEmployee(any(Employee.class))).thenReturn(employee);

        // Perform the POST request
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employee)))
                .andExpect(status().isOk());
    }
    @Test
    void loginCustomerThrowsEmployeeNotFoundException() throws Exception {
        Employee employee = new Employee("Priyanshu", "password");
        when(employeeServices.getEmployee(any(Employee.class))).thenThrow(new EmployeeNotFoundException());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employee)))
                .andExpect(status().isNotFound());
    }


    @Test
    void getEmployeeByNameSuccess() throws Exception {

        when(employeeServices.getEmployeeByName("Priyanshu")).thenReturn(false);

        mockMvc.perform(get("/api/v1/auth/findUser/Priyanshu"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeByNameFailure() throws Exception {
        when(employeeServices.getEmployeeByName("Priyanshu")).thenReturn(false);

        mockMvc.perform(get("/api/v1/auth/findUser/Priyanshu"))
                .andExpect(status().isOk());
    }

    public String convertToJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

