package com.MSPROVEEDORES.MSPROVEEDORES.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.MSPROVEEDORES.MSPROVEEDORES.controller.PedidoProveedorControllerV2;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoProveedorDetalleAssembler implements RepresentationModelAssembler<PedidoProveedorDetalle, EntityModel<PedidoProveedorDetalle>> {

    @Override
    public EntityModel<PedidoProveedorDetalle> toModel(PedidoProveedorDetalle detalle) {
        return EntityModel.of(detalle,
            linkTo(methodOn(PedidoProveedorControllerV2.class)
                .getProducto(
                    detalle.getPedidoProveedor().getIdPedidoProveedor(),
                    detalle.getCodProducto()
                )).withSelfRel(),

            linkTo(methodOn(PedidoProveedorControllerV2.class)
                .obtenerProductosDePedido(detalle.getPedidoProveedor().getIdPedidoProveedor()))
                .withRel("productos-del-pedido")
        );
    }
}
