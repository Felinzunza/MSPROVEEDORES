package com.MSPROVEEDORES.MSPROVEEDORES.assemblers;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.MSPROVEEDORES.MSPROVEEDORES.controller.ProveedorControllerV2;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProveedorAssembler implements RepresentationModelAssembler<Proveedor, EntityModel<Proveedor>>{

    @Override
    public EntityModel<Proveedor> toModel(Proveedor proveedor) {
        return EntityModel.of(proveedor,
                linkTo(methodOn(ProveedorControllerV2.class).getProveedorById(proveedor.getIdProveedor())).withSelfRel(),
                linkTo(methodOn(ProveedorControllerV2.class).getProveedores()).withRel("proveedores")
                );

    }
    
}
