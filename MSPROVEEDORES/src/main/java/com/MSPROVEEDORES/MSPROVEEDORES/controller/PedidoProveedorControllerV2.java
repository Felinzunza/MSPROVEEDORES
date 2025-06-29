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
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
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
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(assembler.toModel(pedido));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoProveedor>> postPedido(@RequestBody PedidoProveedor pedidoProveedor){

        PedidoProveedor buscado = pedidoProveedorService.buscarPedido(pedidoProveedor.getIdPedidoProveedor());
        
        if (buscado == null ) {
            
            if (pedidoProveedor.getProveedor() != null ) { 
                
               Proveedor prov = proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor());
                if (prov != null) {
                    pedidoProveedor.setProveedor(prov);
                } else {
                    return ResponseEntity.badRequest().build();
                }
                
           
            } else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assembler.toModel(pedidoProveedorService.guardarPedido(pedidoProveedor)));

        }
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> deletePedido(@PathVariable int id){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if(pedido == null ){
            return ResponseEntity.notFound().build();
        }
        pedidoProveedorService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value ="/{id}/cambiarEstado", produces = MediaTypes.HAL_JSON_VALUE) //llamarlo desde la url asi: http://localhost:8082/api/v2/pedidosproveedores/1/cambiarEstado?estado=EnviadoAProveedor(o cualquier valor que corresponda al enum)
    public ResponseEntity<EntityModel<PedidoProveedor>> cambiarEstado(@PathVariable int id, @RequestParam EnumEstado estado){
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        pedido.setEstado(estado);
        return ResponseEntity.ok(assembler.toModel(pedidoProveedorService.guardarPedido(pedido)));
    }

    //obtener detalle de productos de un pedido
    @GetMapping(value = "/{id}/productos", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<PedidoProveedorDetalle>> obtenerProductosDePedido(@PathVariable int id) {
    PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
   
    if (pedido == null) {
        return ResponseEntity.notFound().build();
    }
    List<PedidoProveedorDetalle> detalles = pedido.getDetallePedidoProveedor();
    
    // No se usa EntityModel porque PedidoProveedorDetalle no tiene assembler propio, pero puede usarse CollectionModel directo
    return ResponseEntity.ok(
        CollectionModel.of(detalles,
            linkTo(methodOn(PedidoProveedorControllerV2.class).obtenerProductosDePedido(id)).withSelfRel()
        )
    );
}


    //Agregar un producto al pedido
    @PostMapping(value = "/{id}/productos", produces = MediaTypes.HAL_JSON_VALUE) 
    public ResponseEntity<EntityModel<PedidoProveedor>> postProducto(@PathVariable int id, @RequestBody PedidoProveedorDetalle nuevoDetalle) {
        PedidoProveedor pedido = pedidoProveedorService.agregarProducto(id, nuevoDetalle);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(pedido)); // reutiliza el assembler de PedidoProveedor
    }

    //Buscar un producto en el pedido
    @GetMapping(value = "/{id}/productos/{idProducto}", produces = MediaTypes.HAL_JSON_VALUE) //http://localhost:8082/api/v2/pedidosproveedores/{id}/productos/{idProducto}
    public ResponseEntity<CollectionModel<PedidoProveedorDetalle>> getProducto(@PathVariable int id, @PathVariable int idProducto) {
    
    PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
    if (pedido == null) {
        return ResponseEntity.notFound().build();
    }

    List<PedidoProveedorDetalle> detalles = pedido.getDetallePedidoProveedor();
    
    // No se usa EntityModel porque PedidoProveedorDetalle no tiene assembler propio, pero puede usarse CollectionModel directo
    return ResponseEntity.ok(
        CollectionModel.of(detalles,
            linkTo(methodOn(PedidoProveedorControllerV2.class).obtenerProductosDePedido(id)).withSelfRel()
        )
    );
    }

    //Eliminar un producto del pedido
    @DeleteMapping(value = "/{id}/productos/{idProducto}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> deleteProducto(@PathVariable int id, @PathVariable int idProducto) {
        PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
         if (pedido == null) {
        return ResponseEntity.notFound().build();
        }

        PedidoProveedorDetalle detalle = pedidoProveedorService.eliminarProducto(id, idProducto);
        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build(); 
    }

    
    //Cambiar la cantidad de un producto en el pedido
 @PatchMapping(value = "/{id}/productos/{idProducto}", produces = MediaTypes.HAL_JSON_VALUE) //http://localhost:8082/api/v2/pedidosproveedores/{id}/productos/{idProducto}?cantidad=valor
 public ResponseEntity<EntityModel<PedidoProveedorDetalle>> updateProducto(
    @PathVariable int id,
    @PathVariable int idProducto,
    @RequestParam int cantidad) {

    PedidoProveedor pedido = pedidoProveedorService.buscarPedido(id);
    if (pedido == null) {
        return ResponseEntity.notFound().build();
    }
    PedidoProveedorDetalle detalle = pedidoProveedorService.modificarCantidad(id, idProducto, cantidad);
    if (detalle == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(EntityModel.of(detalle));
}
}