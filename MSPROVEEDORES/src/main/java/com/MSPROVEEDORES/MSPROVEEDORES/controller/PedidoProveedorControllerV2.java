package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
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

import com.MSPROVEEDORES.MSPROVEEDORES.assemblers.PedidoProveedorAssembler;
import com.MSPROVEEDORES.MSPROVEEDORES.model.EnumEstado;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;
import com.MSPROVEEDORES.MSPROVEEDORES.service.PedidoProveedorService;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;

@RestController
@RequestMapping("/api/v2/pedidosproveedores")
public class PedidoProveedorControllerV2{

    @Autowired
    private PedidoProveedorService pedidoProveedorService;
    
    @Autowired
    private ProveedorService proveedorService; 

    @Autowired
    private PedidoProveedorAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PedidoProveedor>> getAllPedidos() {
        List<EntityModel<PedidoProveedor>> pedidos = pedidoProveedorService.listaPedidos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoProveedorControllerV2.class).getAllPedidos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoProveedor>> getPedidoXId(@PathVariable int id){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assembler.toModel(pedido), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PedidoProveedor>postPedido(@RequestBody PedidoProveedor pedidoProveedor){
        PedidoProveedor buscado = pedidoProveedorService.buscarPedido(pedidoProveedor.getIdPedidoProveedor());
        if (buscado == null ) {
            
            if (pedidoProveedor.getProveedor() != null ) { /*&& proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor()) != null*/
            pedidoProveedor.setProveedor(
                proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor())
            );
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
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }
    //Buscar un producto en el pedido
    @GetMapping("/{id}/productos/{idProducto}") //http://localhost:8082/api/pedidosproveedores/{id}/productos/{idProducto}
    public ResponseEntity<PedidoProveedorDetalle> getProducto(@PathVariable int id, @PathVariable int idProducto) {
        PedidoProveedorDetalle detalle = pedidoProveedorService.buscarProducto(id, idProducto);
        if (detalle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(detalle, HttpStatus.OK);
    }

    //Eliminar un producto del pedido
@DeleteMapping("/{id}/productos/{idProducto}")
    public ResponseEntity<PedidoProveedorDetalle> deleteProducto(@PathVariable int id, @PathVariable int idProducto) {
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
    
    PedidoProveedorDetalle detalle = pedidoProveedorService.modificarCantidad(id, idProducto, cantidad);
    if (detalle == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(detalle, HttpStatus.OK);
}

}
