package com.MSPROVEEDORES.MSPROVEEDORES.controller;




import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProveedorController.class)
public class ProveedorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;
    private Proveedor proveedor;

    

    @BeforeEach
    public void setup() {
        proveedor = new Proveedor();
        proveedor.setIdProveedor(1);
        proveedor.setNombProveedor("Proveedor 1");
        proveedor.setEmail("proveedor1@example.com");
        proveedor.setTelefono(123456789);
    }

    @Test
    public void testGetProveedores() throws Exception {
        when(proveedorService.getAllProveedores()).thenReturn(List.of(proveedor));

        mockMvc.perform(get("/api/v1/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProveedor").value(proveedor.getIdProveedor()))
                .andExpect(jsonPath("$[0].nombProveedor").value(proveedor.getNombProveedor()))
                .andExpect(jsonPath("$[0].email").value(proveedor.getEmail()))
                .andExpect(jsonPath("$[0].telefono").value(proveedor.getTelefono()));
    }

    @Test
    public void testGetProveedorbyId() throws Exception {
        when(proveedorService.getProveedorById(1)).thenReturn(proveedor);

        mockMvc.perform(get("/api/v1/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProveedor").value(proveedor.getIdProveedor()))
                .andExpect(jsonPath("$.nombProveedor").value(proveedor.getNombProveedor()))
                .andExpect(jsonPath("$.email").value(proveedor.getEmail()))
                .andExpect(jsonPath("$.telefono").value(proveedor.getTelefono()));
    }

    @Test
    public void testGetProveedorByRut() throws Exception {
        when(proveedorService.getProveedorByRut("12345678-9")).thenReturn(proveedor);

        mockMvc.perform(get("/api/v1/proveedores/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProveedor").value(proveedor.getIdProveedor()))
                .andExpect(jsonPath("$.nombProveedor").value(proveedor.getNombProveedor()))
                .andExpect(jsonPath("$.email").value(proveedor.getEmail()))
                .andExpect(jsonPath("$.telefono").value(proveedor.getTelefono()));
    }

    @Test
    public void testPostProveedor() throws Exception {
        when(proveedorService.save(any(Proveedor.class))).thenReturn(proveedor);

        mockMvc.perform(post("/api/v1/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProveedor").value(proveedor.getIdProveedor()))
                .andExpect(jsonPath("$.nombProveedor").value(proveedor.getNombProveedor()))
                .andExpect(jsonPath("$.email").value(proveedor.getEmail()))
                .andExpect(jsonPath("$.telefono").value(proveedor.getTelefono()));
    }

    @Test
    public void testPutProveedor() throws Exception {
        when(proveedorService.getProveedorById(1)).thenReturn(proveedor);
        when(proveedorService.save(any(Proveedor.class))).thenReturn(proveedor);

        mockMvc.perform(put("/api/v1/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProveedor").value(proveedor.getIdProveedor()))
                .andExpect(jsonPath("$.nombProveedor").value(proveedor.getNombProveedor()))
                .andExpect(jsonPath("$.email").value(proveedor.getEmail()))
                .andExpect(jsonPath("$.telefono").value(proveedor.getTelefono()));
    
    }
    @Test
    public void testDeleteProveedor() throws Exception {
        
        when(proveedorService.getProveedorById(1)).thenReturn(proveedor);
        doNothing().when(proveedorService).delete(1);

        mockMvc.perform(delete("/api/v1/proveedores/1"))
                .andExpect(status().isNoContent());
        
        verify(proveedorService, times(1)).delete(1);

    }
}