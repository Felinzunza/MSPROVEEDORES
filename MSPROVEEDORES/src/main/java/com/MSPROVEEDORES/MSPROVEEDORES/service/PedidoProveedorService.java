package com.MSPROVEEDORES.MSPROVEEDORES.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedorDetalle;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.repo.PedidoProveedorRepository;

@Service
public class PedidoProveedorService {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private PedidoProveedorRepository pedidoProveedorRepository;

   

    public List<PedidoProveedor>listaPedidos() {
        return pedidoProveedorRepository.findAll();
    }

    public PedidoProveedor guardarPedido(PedidoProveedor pedidoProveedor) {
        if (pedidoProveedor.getProveedor() != null) {
        Proveedor proveedor = proveedorService.getProveedorById(pedidoProveedor.getProveedor().getIdProveedor());
        pedidoProveedor.setProveedor(proveedor); // enlazar con proveedor existente
        }

        if (pedidoProveedor.getDetallePedidoProveedor() != null) { //for agregado el 18/05 para enlazar con detalle y crear pedido directamente con detalles
            for (PedidoProveedorDetalle detalle : pedidoProveedor.getDetallePedidoProveedor()) {
                detalle.setPedidoProveedor(pedidoProveedor);
            }
        }
        return pedidoProveedorRepository.save(pedidoProveedor);
    }

    public PedidoProveedor buscarPedido(int idPedidoProveedor) {
        return pedidoProveedorRepository.findById(idPedidoProveedor);
    }

    public void eliminarPedido(int idPedidoProveedor) {
        pedidoProveedorRepository.deleteById(idPedidoProveedor);
    }


    //METODOS PARA LOS PRODUCTOS DEL PEDIDO
    //Agregar un producto al pedido
    public PedidoProveedor agregarProducto(int idPedido, PedidoProveedorDetalle nuevoDetalle) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(idPedido);
        if (pedido == null) return null;

        // Enlazar el detalle al pedido
        nuevoDetalle.setPedidoProveedor(pedido); //esto me falto

        // Agregar el detalle a la lista
        pedido.getDetallePedidoProveedor().add(nuevoDetalle);
        return pedidoProveedorRepository.save(pedido);
        }
    
    //Buscar un producto en el pedido
    public PedidoProveedorDetalle buscarProducto(int idPedido, int idProducto) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(idPedido);
        if (pedido == null) { // Si no se encuentra el pedido, retornamos null
            return null;
        }
        // Buscamos el producto en la lista de detalles del pedido
        for (PedidoProveedorDetalle detalle : pedido.getDetallePedidoProveedor()) {
            if (detalle.getCod_Producto() == idProducto) {
                return detalle;
            }
        }
        return null;
    
    }

    //Eliminar un producto del pedido
    public PedidoProveedorDetalle eliminarProducto(int idPedido, int idProducto) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(idPedido);
        if (pedido == null) return null;

        PedidoProveedorDetalle detalleAEliminar = null;
        for (PedidoProveedorDetalle detalle : pedido.getDetallePedidoProveedor()) {
            if (detalle.getCod_Producto() == idProducto) {
                detalleAEliminar = detalle;
                break;
            }
        }

        if (detalleAEliminar != null) {
            pedido.getDetallePedidoProveedor().remove(detalleAEliminar);
            pedidoProveedorRepository.save(pedido);
        }
        return detalleAEliminar;
    }

    //Modificar cantidad de un producto en el pedido
    public PedidoProveedorDetalle modificarCantidad(int idPedido, int idProducto, int nuevaCantidad) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(idPedido);
        if (pedido == null) return null;

        for (PedidoProveedorDetalle detalle : pedido.getDetallePedidoProveedor()) {
            if (detalle.getCod_Producto() == idProducto) {
                detalle.setCantidad(nuevaCantidad);
                pedidoProveedorRepository.save(pedido);
                return detalle;
            }
        }
        return null;

    }
}