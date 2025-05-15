package com.MSPROVEEDORES.MSPROVEEDORES.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.repo.PedidoProveedorRepository;

@Service
public class PedidoProveedorService {
    
    private PedidoProveedorRepository pedidoProveedorRepository;

    public List<PedidoProveedor>listaPedidos() {
        return pedidoProveedorRepository.findAll();
    }

    public PedidoProveedor guardarPedido(PedidoProveedor pedidoProveedor) {
        return pedidoProveedorRepository.save(pedidoProveedor);
    }

    public PedidoProveedor buscarPedido(int idPedidoProveedor) {
        return pedidoProveedorRepository.findById(idPedidoProveedor);
    }

    public void eliminarPedido(int idPedidoProveedor) {
        pedidoProveedorRepository.deleteById(idPedidoProveedor);
    }
}
