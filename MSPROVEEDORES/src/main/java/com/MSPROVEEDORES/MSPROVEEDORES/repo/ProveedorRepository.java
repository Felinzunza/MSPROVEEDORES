package com.MSPROVEEDORES.MSPROVEEDORES.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;



@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    

    Proveedor findById(int idProveedor);
    List<Proveedor>findByRut(String rut);
    
    
}
