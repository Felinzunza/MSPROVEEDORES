package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.PedidoProveedorService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoProveedorController {

    @Autowired
    private PedidoProveedorService pedidoProveedorService;
    

    @GetMapping
    public ResponseEntity<List<PedidoProveedor>>getAllPedidos(){
        List<PedidoProveedor>pedidos = pedidoProveedorService.listaPedidos();
        if (pedidos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.ACCEPTED);


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
        if (buscado == null ) {
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








    
}
