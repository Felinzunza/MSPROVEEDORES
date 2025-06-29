package com.MSPROVEEDORES.MSPROVEEDORES.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.MSPROVEEDORES.MSPROVEEDORES.controller.PedidoProveedorControllerV2;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoProveedorAssembler implements RepresentationModelAssembler<PedidoProveedor, EntityModel<PedidoProveedor>>{

    @Override
    public EntityModel<PedidoProveedor> toModel(PedidoProveedor pedidoProveedor) {
        return EntityModel.of(pedidoProveedor,
                linkTo(methodOn(PedidoProveedorControllerV2.class).getPedidoXId(pedidoProveedor.getIdPedidoProveedor())).withSelfRel(),
                linkTo(methodOn(PedidoProveedorControllerV2.class).getAllPedidos()).withRel("pedidosproveedores")
                );

    }
    
}