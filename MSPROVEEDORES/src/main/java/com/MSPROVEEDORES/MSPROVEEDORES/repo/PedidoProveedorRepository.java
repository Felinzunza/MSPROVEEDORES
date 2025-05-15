package com.MSPROVEEDORES.MSPROVEEDORES.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MSPROVEEDORES.MSPROVEEDORES.model.PedidoProveedor;

@Repository
public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Integer> {
    
     
     PedidoProveedor findById(int idPedidoProveedor);
    
}
