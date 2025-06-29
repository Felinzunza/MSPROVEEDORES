package com.MSPROVEEDORES.MSPROVEEDORES.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MSPROVEEDORES.MSPROVEEDORES.assemblers.ProveedorAssembler;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.service.ProveedorService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/v2/proveedores")
public class ProveedorControllerV2 {
    
    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProveedorAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Proveedor>>getProveedores(){
        List<EntityModel<Proveedor>>proveedores = proveedorService.getAllProveedores().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

        if (proveedores.isEmpty()) {
            return CollectionModel.empty();
        }

        return CollectionModel.of(proveedores,
                linkTo(methodOn(ProveedorControllerV2.class).getProveedores()).withSelfRel());

    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Proveedor>> getProveedorById(@PathVariable int id) {
        Proveedor proveedor = proveedorService.getProveedorById(id);
        if (proveedor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(proveedor));
    }

    @GetMapping(value = "/rut/{rut}", produces = MediaTypes.HAL_JSON_VALUE) 
    public ResponseEntity<EntityModel<Proveedor>> getProveedorByRut(@PathVariable String rut) {
        Proveedor proveedor = proveedorService.getProveedorByRut(rut);
        if (proveedor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(proveedor));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Proveedor>>postProveedor(@RequestBody Proveedor proveedor) {
        
        Proveedor nuevoProveedor = proveedorService.save(proveedor);
        if (nuevoProveedor == null) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity
            .created(linkTo(methodOn(ProveedorControllerV2.class).getProveedorById(nuevoProveedor.getIdProveedor())).toUri())
            .body(assembler.toModel(nuevoProveedor));        
        
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Proveedor>>putProveedor(@PathVariable int id, @RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(id); //esto es para evitar que se autogenere?
        
        if (proveedorService.getProveedorById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Proveedor updatedProveedor = proveedorService.save(proveedor); //por que no va antes del if si ese objeto es que voy retornar
        return ResponseEntity.ok(assembler.toModel(updatedProveedor));
    }


    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> deleteProveedor(@PathVariable int id) {
        Proveedor proveedor = proveedorService.getProveedorById(id);
        if (proveedor == null) {
            return ResponseEntity.notFound().build();
        }
        proveedorService.delete(id); //entiendo, entonces es lo mismo que aqui, despues que se verifica que existe elimino el proveedor
        return ResponseEntity.noContent().build();
    }
    

}
