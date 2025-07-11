package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.MSPROVEEDORES.MSPROVEEDORES.model.EnumEstado;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.PedidoProveedorService;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

    @Autowired
    private ObjectMapper objectMapper;
    private PedidoProveedor pedidoProveedor;
    private PedidoProveedorDetalle pedidoProveedorDetalle;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pedidoProveedorDetalle = new PedidoProveedorDetalle();
        pedidoProveedorDetalle.setIdDetalle(1);
        pedidoProveedorDetalle.setCodProducto(1);
        pedidoProveedorDetalle.setCantidad(10);


        pedidoProveedor = new PedidoProveedor();
        pedidoProveedor.setIdPedidoProveedor(1);
        pedidoProveedor.setIdTienda(1);
        pedidoProveedor.setFechaEmision(LocalDate.now());
        pedidoProveedor.setFechaRecepcionEsperada(LocalDate.now().plusDays(7));
        pedidoProveedor.setEstado(EnumEstado.Iniciado);
        pedidoProveedor.setDetallePedidoProveedor(new ArrayList<>());
        pedidoProveedor.getDetallePedidoProveedor().add(pedidoProveedorDetalle);


        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(1);
        pedidoProveedor.setProveedor(proveedor);

    }
    
    @Test
    public void testGetAllPedidos() throws Exception {
        when(pedidoProveedorService.listaPedidos()).thenReturn(List.of(pedidoProveedor));

        mockMvc.perform(get("/api/v1/pedidosproveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedidoProveedor").value(pedidoProveedor.getIdPedidoProveedor()))
                .andExpect(jsonPath("$[0].idTienda").value(pedidoProveedor.getIdTienda()))
                .andExpect(jsonPath("$[0].fechaEmision").value(pedidoProveedor.getFechaEmision().toString()))
                .andExpect(jsonPath("$[0].fechaRecepcionEsperada").value(pedidoProveedor.getFechaRecepcionEsperada().toString()))
                .andExpect(jsonPath("$[0].estado").value(pedidoProveedor.getEstado().toString()))
                .andExpect(jsonPath("$[0].proveedor.idProveedor").value(pedidoProveedor.getProveedor().getIdProveedor()))
                .andExpect(jsonPath("$[0].detallePedidoProveedor[0].idDetalle").value(1))
                .andExpect(jsonPath("$[0].detallePedidoProveedor[0].codProducto").value(1))
                .andExpect(jsonPath("$[0].detallePedidoProveedor[0].cantidad").value(10));

        when(pedidoProveedorService.listaPedidos()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/pedidosproveedores"))
           .andExpect(status().isNoContent());
    }

    
    @Test
    public void testGetPedidoXId() throws Exception {

        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);

        mockMvc.perform(get("/api/v1/pedidosproveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedidoProveedor").value(pedidoProveedor.getIdPedidoProveedor()))
                .andExpect(jsonPath("$.idTienda").value(pedidoProveedor.getIdTienda()))
                .andExpect(jsonPath("$.fechaEmision").value(pedidoProveedor.getFechaEmision().toString()))
                .andExpect(jsonPath("$.fechaRecepcionEsperada").value(pedidoProveedor.getFechaRecepcionEsperada().toString()))
                .andExpect(jsonPath("$.estado").value(pedidoProveedor.getEstado().toString()))
                .andExpect(jsonPath("$.proveedor.idProveedor").value(pedidoProveedor.getProveedor().getIdProveedor()))
                .andExpect(jsonPath("$.detallePedidoProveedor[0].idDetalle").value(pedidoProveedorDetalle.getIdDetalle()))
                .andExpect(jsonPath("$.detallePedidoProveedor[0].codProducto").value(pedidoProveedorDetalle.getCodProducto()))
                .andExpect(jsonPath("$.detallePedidoProveedor[0].cantidad").value(pedidoProveedorDetalle.getCantidad()));
        
        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/pedidosproveedores/99"))
                .andExpect(status().isNotFound());

    }

  

    @Test
    public void testPostPedido() throws Exception {
    // Configurar mock del proveedor existente
    when(proveedorService.getProveedorById(1)).thenReturn(pedidoProveedor.getProveedor());
    when(pedidoProveedorService.buscarPedido(1)).thenReturn(null); // Simula que no existe aún
    when(pedidoProveedorService.guardarPedido(any(PedidoProveedor.class))).thenReturn(pedidoProveedor);

    mockMvc.perform(post("/api/v1/pedidosproveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoProveedor)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idPedidoProveedor").value(pedidoProveedor.getIdPedidoProveedor()))
            .andExpect(jsonPath("$.idTienda").value(pedidoProveedor.getIdTienda()))
            .andExpect(jsonPath("$.fechaEmision").value(pedidoProveedor.getFechaEmision().toString()))
            .andExpect(jsonPath("$.fechaRecepcionEsperada").value(pedidoProveedor.getFechaRecepcionEsperada().toString()))
            .andExpect(jsonPath("$.estado").value(pedidoProveedor.getEstado().toString()))
            .andExpect(jsonPath("$.proveedor.idProveedor").value(pedidoProveedor.getProveedor().getIdProveedor()))
            .andExpect(jsonPath("$.detallePedidoProveedor[0].idDetalle").value(pedidoProveedorDetalle.getIdDetalle()))
            .andExpect(jsonPath("$.detallePedidoProveedor[0].codProducto").value(pedidoProveedorDetalle.getCodProducto()))
            .andExpect(jsonPath("$.detallePedidoProveedor[0].cantidad").value(pedidoProveedorDetalle.getCantidad()));
    }

    @Test
    public void testPostPedido_ProveedorNulo() throws Exception {
    pedidoProveedor.setProveedor(null);

    mockMvc.perform(post("/api/v1/pedidosproveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoProveedor)))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void testPostPedido_ProveedorNoExiste() throws Exception {
    when(pedidoProveedorService.buscarPedido(1)).thenReturn(null);
    when(proveedorService.getProveedorById(1)).thenReturn(null); // proveedor no encontrado

    mockMvc.perform(post("/api/v1/pedidosproveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoProveedor)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostPedido_ConflictoPedidoYaExiste() throws Exception {
    when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor); // ya existe

    mockMvc.perform(post("/api/v1/pedidosproveedores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pedidoProveedor)))
            .andExpect(status().isConflict());
    }


    @Test
    public void testDeletePedido() throws Exception {
        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        doNothing().when(pedidoProveedorService).eliminarPedido(1);

        mockMvc.perform(delete("/api/v1/pedidosproveedores/1"))
                .andExpect(status().isNoContent());

        verify(pedidoProveedorService, times(1)).eliminarPedido(1);
        
        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(delete("/api/v1/pedidosproveedores/99"))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testCambiarEstado() throws Exception {
        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        when(pedidoProveedorService.guardarPedido(any(PedidoProveedor.class))).thenReturn(pedidoProveedor);

        mockMvc.perform(patch("/api/v1/pedidosproveedores/1/cambiarEstado?estado=EnviadoAProveedor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(EnumEstado.EnviadoAProveedor.toString()));

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(patch("/api/v1/pedidosproveedores/99/cambiarEstado?estado=EnviadoAProveedor"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testObtenerProductosDePedido() throws Exception {
        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        mockMvc.perform(get("/api/v1/pedidosproveedores/1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDetalle").value(pedidoProveedorDetalle.getIdDetalle()))
                .andExpect(jsonPath("$[0].codProducto").value(pedidoProveedorDetalle.getCodProducto())) //este es producto
                .andExpect(jsonPath("$[0].cantidad").value(pedidoProveedorDetalle.getCantidad()));
        verify(pedidoProveedorService, times(1)).buscarPedido(1);

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null); // 
        mockMvc.perform(get("/api/v1/pedidosproveedores/99/productos"))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testPostProducto() throws Exception {
        PedidoProveedorDetalle nuevoDetalle = new PedidoProveedorDetalle();
        nuevoDetalle.setIdDetalle(2);
        nuevoDetalle.setCodProducto(2);
        nuevoDetalle.setCantidad(5);

        // Simular que el producto fue agregado
        pedidoProveedor.getDetallePedidoProveedor().add(nuevoDetalle);

        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        when(pedidoProveedorService.agregarProducto(eq(1), any(PedidoProveedorDetalle.class)))
            .thenReturn(pedidoProveedor);

        mockMvc.perform(post("/api/v1/pedidosproveedores/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoDetalle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.detallePedidoProveedor[1].idDetalle").value(2))
                .andExpect(jsonPath("$.detallePedidoProveedor[1].codProducto").value(2))
                .andExpect(jsonPath("$.detallePedidoProveedor[1].cantidad").value(5));

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(post("/api/v1/pedidosproveedores/99/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoDetalle)))
                .andExpect(status().isNotFound());

     }
    

    @Test
    public void testGetProducto() throws Exception {
        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        when(pedidoProveedorService.buscarProducto(1, 1)).thenReturn(pedidoProveedorDetalle);

        mockMvc.perform(get("/api/v1/pedidosproveedores/1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetalle").value(pedidoProveedorDetalle.getIdDetalle()))
                .andExpect(jsonPath("$.codProducto").value(pedidoProveedorDetalle.getCodProducto()))
                .andExpect(jsonPath("$.cantidad").value(pedidoProveedorDetalle.getCantidad()));

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/pedidosproveedores/99/productos/1"))
        .andExpect(status().isNotFound());

        when(pedidoProveedorService.buscarProducto(1, 99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/pedidosproveedores/1/productos/99"))
           .andExpect(status().isNotFound());
    }



 

    @Test
    public void testDeleteProducto() throws Exception {
        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        when(pedidoProveedorService.buscarProducto(1, 1)).thenReturn(pedidoProveedorDetalle);
        //doNothing().when(pedidoProveedorService).eliminarProducto(1, 1);
        when(pedidoProveedorService.eliminarProducto(1, 1)).thenReturn(pedidoProveedorDetalle);

        mockMvc.perform(delete("/api/v1/pedidosproveedores/1/productos/1"))
                .andExpect(status().isOk());

        verify(pedidoProveedorService, times(1)).eliminarProducto(1, 1);

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(delete("/api/v1/pedidosproveedores/99/productos/1"))
        .andExpect(status().isNotFound());

        when(pedidoProveedorService.buscarProducto(1, 99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/pedidosproveedores/1/productos/99"))
        .andExpect(status().isNotFound());
    }

 

    @Test
    public void testUpdateProducto() throws Exception {

        when(pedidoProveedorService.buscarPedido(1)).thenReturn(pedidoProveedor);
        when(pedidoProveedorService.buscarProducto(1, 1)).thenReturn(pedidoProveedorDetalle);

        PedidoProveedorDetalle updatedDetalle = new PedidoProveedorDetalle();
        updatedDetalle.setIdDetalle(1);
        updatedDetalle.setCodProducto(1);
        updatedDetalle.setCantidad(20); 

        when(pedidoProveedorService.modificarCantidad(1, 1, 20)).thenReturn(updatedDetalle);

        mockMvc.perform(patch("/api/v1/pedidosproveedores/1/productos/1")
                .param("cantidad", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetalle").value(updatedDetalle.getIdDetalle()))
                .andExpect(jsonPath("$.codProducto").value(updatedDetalle.getCodProducto()))
                .andExpect(jsonPath("$.cantidad").value(updatedDetalle.getCantidad()));
        verify(pedidoProveedorService, times(1)).modificarCantidad(1, 1, 20);

        when(pedidoProveedorService.buscarPedido(99)).thenReturn(null);
        mockMvc.perform(patch("/api/v1/pedidosproveedores/99/productos/1")
        .param("cantidad", "20"))
        .andExpect(status().isNotFound());

        when(pedidoProveedorService.buscarProducto(1, 99)).thenReturn(null);
        mockMvc.perform(patch("/api/v1/pedidosproveedores/1/productos/99")
        .param("cantidad", "20"))
        .andExpect(status().isNotFound());

     
    }





}