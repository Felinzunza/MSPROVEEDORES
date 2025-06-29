package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MSPROVEEDORES.MSPROVEEDORES.model.EnumEstado;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.PedidoProveedorService;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;

@RestController
@RequestMapping("/api/v1/pedidosproveedores")
public class PedidoProveedorController {

    @Autowired
    private PedidoProveedorService pedidoProveedorService;
    
    @Autowired
    private ProveedorService proveedorService; 

    @GetMapping
    public ResponseEntity<List<PedidoProveedor>>getAllPedidos(){
        List<PedidoProveedor>pedidos = pedidoProveedorService.listaPedidos();
        if (pedidos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);


    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoProveedor>getPedidoXId(@PathVariable int id){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PedidoProveedor>postPedido(@RequestBody PedidoProveedor pedidoProveedor){
        PedidoProveedor buscado = pedidoProveedorService.buscarPedido(pedidoProveedor.getIdPedidoProveedor());

        // Verificar si el pedido ya existe
        if (buscado == null ) {
            // Verificar si el proveedor está presente en el pedido
            if (pedidoProveedor.getProveedor() != null ) { 
                
            // Verificar si el proveedor existe
            Proveedor proveedorExistente = proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor());
            // Si el proveedor existe, asignarlo al pedido
            if (proveedorExistente != null) {
                pedidoProveedor.setProveedor(
                proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor())
            );
            } else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            
        }
        
        return new ResponseEntity<>(pedidoProveedorService.guardarPedido(pedidoProveedor), HttpStatus.CREATED);
        
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deletePedido(@PathVariable int id){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if(pedido == null ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pedidoProveedorService.eliminarPedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/cambiarEstado") //llamarlo desde la url asi: http://localhost:8082/api/pedidosproveedores/1/cambiarEstado?estado=EnviadoAProveedor(o cualquier valor que corresponda al enum)
    public ResponseEntity<PedidoProveedor>cambiarEstado(@PathVariable int id, @RequestParam EnumEstado estado){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pedido.setEstado(estado);
        return new ResponseEntity<>(pedidoProveedorService.guardarPedido(pedido), HttpStatus.OK);
    }

    //obtener detalle de un pedido
    @GetMapping("/{id}/productos")
    public ResponseEntity<List<PedidoProveedorDetalle>> obtenerProductosDePedido(@PathVariable int id) {
    PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
    if (pedido == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    return new ResponseEntity<>(pedido.getDetallePedidoProveedor(), HttpStatus.OK);
    }


    //Agregar un producto al pedido
    @PostMapping("/{id}/productos") //agregarlo asi: 
    public ResponseEntity<PedidoProveedor> postProducto(@PathVariable int id, @RequestBody PedidoProveedorDetalle nuevoDetalle) {
        PedidoProveedor pedido = pedidoProveedorService.agregarProducto(id, nuevoDetalle);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }
    //Buscar un producto en el pedido
    @GetMapping("/{id}/productos/{idProducto}") //http://localhost:8082/api/pedidosproveedores/{id}/productos/{idProducto}
    public ResponseEntity<PedidoProveedorDetalle> getProducto(@PathVariable int id, @PathVariable int idProducto) {

        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PedidoProveedorDetalle detalle = pedidoProveedorService.buscarProducto(id, idProducto);
        if (detalle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    //Eliminar un producto del pedido
@DeleteMapping("/{id}/productos/{idProducto}")
    public ResponseEntity<PedidoProveedorDetalle> deleteProducto(@PathVariable int id, @PathVariable int idProducto) {

        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PedidoProveedorDetalle detalle = pedidoProveedorService.eliminarProducto(id, idProducto);
        if (detalle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }
    
    //Cambiar la cantidad de un producto en el pedido
 @PatchMapping("/{id}/productos/{idProducto}") //http://localhost:8082/api/pedidosproveedores/{id}/productos/{idProducto}?cantidad=valor

 public ResponseEntity<PedidoProveedorDetalle> updateProducto(
    @PathVariable int id,
    @PathVariable int idProducto,
    @RequestParam int cantidad) {

    PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
    PedidoProveedorDetalle detalle = pedidoProveedorService.modificarCantidad(id, idProducto, cantidad);
    if (detalle == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(detalle, HttpStatus.OK);
}

}
