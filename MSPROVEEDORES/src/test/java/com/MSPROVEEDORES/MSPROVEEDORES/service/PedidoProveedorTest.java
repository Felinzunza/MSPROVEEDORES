package com.MSPROVEEDORES.MSPROVEEDORES.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.MSPROVEEDORES.MSPROVEEDORES.model.EnumEstado;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.repo.PedidoProveedorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PedidoProveedorTest {

    @Mock
    private PedidoProveedorRepository pedidoproveedorRepository;

    @Mock
    private ProveedorService proveedorService;
    
    @InjectMocks
    private PedidoProveedorService pedidoProveedorService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarPedidoProveedor(){

        //Objeto creado
        Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ECOSAS@GMAIL.COM");


        PedidoProveedor pedidoProv = new PedidoProveedor(0, 101, LocalDate.of(2025, 01, 01), LocalDate.of(2025, 01, 07), new ArrayList<>(), EnumEstado.Iniciado, prov);
        
        PedidoProveedorDetalle detalle = new PedidoProveedorDetalle(
            0,            // idDetalle (autogenerado, por eso puede ser 0)
            3,            // idInventario
            50,           // cantidad
            pedidoProv        // asignamos el pedido al que pertenece (clave)
        );

        
        pedidoProv.setDetallePedidoProveedor(List.of(detalle));
        

        //Objeto guardado esperado

        Proveedor prov2 = new Proveedor(1, "123123123-9", "ECOSAS", 76543321, "ECOSAS@GMAIL.COM");


        PedidoProveedor pedidoProvGuardado = new PedidoProveedor(1, 101, LocalDate.of(2025, 01, 01), LocalDate.of(2025, 01, 07), new ArrayList<>(), EnumEstado.Iniciado, prov2);
        
        PedidoProveedorDetalle detalle2 = new PedidoProveedorDetalle(
            1,            
            3,           
            50,           
            pedidoProvGuardado      
        );
        pedidoProvGuardado.setDetallePedidoProveedor(List.of(detalle2));

        when(pedidoproveedorRepository.save(pedidoProv)).thenReturn(pedidoProvGuardado);
        when(proveedorService.getProveedorById(1)).thenReturn(prov); 


        PedidoProveedor resultado = pedidoProveedorService.guardarPedido(pedidoProv);

        assertThat(resultado.getIdPedidoProveedor()).isEqualTo(1);
        assertThat(resultado.getIdTienda()).isEqualTo(101);
        assertThat(resultado.getDetallePedidoProveedor()).isEqualTo(List.of(detalle2));
        assertThat(resultado.getEstado()).isEqualTo(EnumEstado.Iniciado);
        assertThat(resultado.getFechaEmision()).isEqualTo(LocalDate.of(2025, 01, 01));
        assertThat(resultado.getFechaRecepcionEsperada()).isEqualTo(LocalDate.of(2025, 01, 07));
        assertThat(resultado.getProveedor()).isEqualTo(prov2);
        verify(pedidoproveedorRepository).save(pedidoProv);

    }

   @Test
    void testListarTodosLosPedidos() {
    Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ecosas@gmail.com");

    PedidoProveedor pedido1 = new PedidoProveedor(
        1, 101, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7),
        new ArrayList<>(), EnumEstado.Iniciado, prov
    );

        PedidoProveedorDetalle detalle = new PedidoProveedorDetalle(
            1,            // idDetalle (autogenerado, por eso puede ser 0)
            3,            // idInventario
            50,           // cantidad
            pedido1        // asignamos el pedido al que pertenece (clave)
        );

        
        pedido1.setDetallePedidoProveedor(List.of(detalle));

    PedidoProveedor pedido2 = new PedidoProveedor(
        2, 102, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10),
        new ArrayList<>(), EnumEstado.RecibidoEnTienda, prov
    );

        PedidoProveedorDetalle detalle2 = new PedidoProveedorDetalle(
            2,            // idDetalle (autogenerado, por eso puede ser 0)
            4,            // idInventario
            40,           // cantidad
            pedido2        // asignamos el pedido al que pertenece (clave)
        );

        
        pedido2.setDetallePedidoProveedor(List.of(detalle2));

    when(pedidoproveedorRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

    List<PedidoProveedor> resultado = pedidoProveedorService.listaPedidos();

    assertThat(resultado).hasSize(2).containsExactly(pedido1, pedido2);
    verify(pedidoproveedorRepository).findAll();
    
    }

    @Test
    void TestBuscarPedidoXId(){
    
    int idPedido = 1;
    Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ecosas@gmail.com");

    PedidoProveedor pedidobuscado = new PedidoProveedor(
        idPedido, 101, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7),
        new ArrayList<>(), EnumEstado.Iniciado, prov
    );

        PedidoProveedorDetalle detalle = new PedidoProveedorDetalle(
            0,            // idDetalle (autogenerado, por eso puede ser 0)
            3,            // idInventario
            50,           // cantidad
            pedidobuscado        // asignamos el pedido al que pertenece (clave)
        );

        
    pedidobuscado.setDetallePedidoProveedor(List.of(detalle));

    when(pedidoproveedorRepository.findById(idPedido)).thenReturn(pedidobuscado);

    PedidoProveedor resultado = pedidoProveedorService.buscarPedido(idPedido);
    assertThat(resultado).isNotNull();
    assertThat(resultado.getDetallePedidoProveedor()).isNotNull();
    verify(pedidoproveedorRepository).findById(idPedido);

    }

    @Test
    void TestEliminarPedidoXId(){
    int idProveedor = 1;
    doNothing().when(pedidoproveedorRepository).deleteById(idProveedor);

    pedidoProveedorService.eliminarPedido(idProveedor);

    verify(pedidoproveedorRepository).deleteById(idProveedor);

    }

    @Test
    void TestAgregarProductoAlPedido(){

        int idPedido = 0;
        Proveedor prov = new Proveedor(1, "123123123-9", "ECOSAS", 76543321, "ECOSAS@GMAIL.COM");

        ArrayList<PedidoProveedorDetalle>detalle = new ArrayList<>();

        PedidoProveedor pedidoProv = new PedidoProveedor(
            idPedido,
            101,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 7),
            detalle, // ← aún está vacía
            EnumEstado.Iniciado,
            prov
        );

        PedidoProveedorDetalle nuevoDetalle = new PedidoProveedorDetalle(
            0, 5, 20, pedidoProv
        );

        detalle.add(nuevoDetalle); // Agregamos el nuevo detalle al pedido
        pedidoProv.setDetallePedidoProveedor(detalle); // Actualizamos el pedido con el nuevo detalle


        // Simula el pedido "guardado" con el detalle con ID autogenerado
        PedidoProveedorDetalle detalleGuardado = new PedidoProveedorDetalle(
            1, // ID autogenerado
            5,
            20,
            pedidoProv
        );
        ArrayList<PedidoProveedorDetalle> detalleGuardadoList = new ArrayList<>();
        detalleGuardadoList.add(detalleGuardado);

        PedidoProveedor pedidoProvGuardado = new PedidoProveedor(
            idPedido,
            101,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 7),
            detalleGuardadoList,
            EnumEstado.Iniciado,
            prov
        );

        when(pedidoproveedorRepository.findById(idPedido)).thenReturn(pedidoProv);
        when(pedidoproveedorRepository.save(pedidoProv)).thenReturn(pedidoProvGuardado);

        PedidoProveedor resultado = pedidoProveedorService.agregarProducto(idPedido, nuevoDetalle);

        List<PedidoProveedorDetalle> detallesResultado = resultado.getDetallePedidoProveedor();


        PedidoProveedorDetalle detallep = detallesResultado.get(0);
        assertThat(detallep.getIdDetalle()).isEqualTo(1); 
        assertThat(detallep.getIdInventario()).isEqualTo(5);
        assertThat(detallep.getCantidad()).isEqualTo(20);
        verify(pedidoproveedorRepository).save(pedidoProv);

    }

    @Test
    void TestBuscarProductoDelPedido(){
        int idPedido = 1;
        int idProducto = 101;
        Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ecosas@gmail.com");
        PedidoProveedor pedido = new PedidoProveedor(
            idPedido, 101, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7),
            new ArrayList<>(), EnumEstado.Iniciado, prov
        );

        PedidoProveedorDetalle detalle = new PedidoProveedorDetalle(
            1, idProducto, 50, pedido
        );
        pedido.setDetallePedidoProveedor(List.of(detalle));


        when(pedidoproveedorRepository.findById(idPedido)).thenReturn(pedido);

        PedidoProveedorDetalle detalleBuscado = pedidoProveedorService.buscarProducto(idPedido, idProducto);


       //quiero verificar que el idinventario me devuelva el producto correcto
       assertThat(detalleBuscado.getIdInventario()).isEqualTo(101);
       assertThat(detalleBuscado.getCantidad()).isEqualTo(50);
       assertThat(detalleBuscado.getPedidoProveedor()).isEqualTo(pedido);
       verify(pedidoproveedorRepository).findById(idPedido);





    }

    @Test
    void TestAjustarCantidadProducto(){
        int idPedido = 1;
        int idProducto = 101;
        Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ecosas@gmail.com");
        PedidoProveedor pedido = new PedidoProveedor(
            idPedido, 101, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7),
            new ArrayList<>(), EnumEstado.Iniciado, prov
        );
        PedidoProveedorDetalle detalle = new PedidoProveedorDetalle(
            1, idProducto, 50, pedido
        );
        pedido.setDetallePedidoProveedor(List.of(detalle));

        when(pedidoproveedorRepository.findById(idPedido)).thenReturn(pedido);

        PedidoProveedorDetalle detalleAjustado = pedidoProveedorService.modificarCantidad(idPedido, idProducto, 30);

        // Verificar que la cantidad se haya ajustado correctamente
        assertThat(detalleAjustado.getCantidad()).isEqualTo(30);
        verify(pedidoproveedorRepository).findById(idPedido);

    }

    @Test
    void TestEliminarProductoDelPedido(){
        int idPedido = 0;
        int idProducto = 101;
        Proveedor prov = new Proveedor(1, "123123123-9", "ECOSAS", 76543321, "ECOSAS@GMAIL.COM");

        ArrayList<PedidoProveedorDetalle>detalle = new ArrayList<>();

        PedidoProveedor pedido = new PedidoProveedor(
            idPedido,
            101,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 7),
            detalle, // ← aún está vacía
            EnumEstado.Iniciado,
            prov
        );

        PedidoProveedorDetalle nuevoDetalle = new PedidoProveedorDetalle(
            0, idProducto, 20, pedido
        );

        detalle.add(nuevoDetalle); // Agregamos el nuevo detalle al pedido
        pedido.setDetallePedidoProveedor(detalle);
        
        when(pedidoproveedorRepository.findById(idPedido)).thenReturn(pedido);

        // Simular la eliminación del producto
        pedidoProveedorService.eliminarProducto(idPedido, idProducto);

        assertThat(pedido.getDetallePedidoProveedor()).doesNotContain(nuevoDetalle);
        verify(pedidoproveedorRepository).findById(idPedido);
        
        

        
    }



    




    
    
    
}



    

