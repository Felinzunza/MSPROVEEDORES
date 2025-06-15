package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    
    @GetMapping
    public ResponseEntity<List<Proveedor>>getProveedores(){
        List<Proveedor>proveedores = proveedorService.getAllProveedores();
        if (proveedores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(proveedores, HttpStatus.ACCEPTED);
        
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable int id) {
        Proveedor proveedor = proveedorService.getProveedorById(id);
        if (proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/rut/{rut}") 
    public ResponseEntity<Proveedor>getProveedorByRut(@PathVariable String rut) {
        Proveedor proveedor = proveedorService.getProveedorByRut(rut);
        if (proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proveedor>postProveedor(@RequestBody Proveedor proveedor) {
        Proveedor buscado = proveedorService.getProveedorById(proveedor.getIdProveedor());
        if (buscado == null) {
            return new ResponseEntity<>(proveedorService.save(proveedor), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> putProveedor(@PathVariable int id, @RequestBody Proveedor proveedor) {
        Proveedor existente = proveedorService.getProveedorById(id);
        if (existente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        proveedor.setIdProveedor(id);
        return new ResponseEntity<>(proveedorService.save(proveedor), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable int id) {
        Proveedor proveedor = proveedorService.getProveedorById(id);
        if (proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        proveedorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
}
