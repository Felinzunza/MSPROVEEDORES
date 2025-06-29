package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.MSPROVEEDORES.MSPROVEEDORES.service.PedidoProveedorService;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PedidoProveedorController.class)
public class PedidoProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoProveedorService pedidoProveedorService;

    @MockBean
    private ProveedorService proveedorService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    

}
