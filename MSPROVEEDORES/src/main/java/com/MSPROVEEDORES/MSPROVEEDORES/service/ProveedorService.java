package com.MSPROVEEDORES.MSPROVEEDORES.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.repo.ProveedorRepository;

@Service
public class ProveedorService {
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    

    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor getProveedorById(int id) {

        return proveedorRepository.findById(id);

    }

    public List<Proveedor> getProveedorByRut(String rut) {

        return proveedorRepository.findByRut(rut);
    }

    
    
    public void delete(int id) {
        proveedorRepository.deleteById(id);
    }
    

    
  
}
