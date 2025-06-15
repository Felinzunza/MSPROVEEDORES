package com.MSPROVEEDORES.MSPROVEEDORES.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.MSPROVEEDORES.MSPROVEEDORES.model.Proveedor;
import com.MSPROVEEDORES.MSPROVEEDORES.repo.ProveedorRepository;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;
    
    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarProveedor(){
    
    Proveedor prov = new Proveedor(0, "123123123-9", "ECOSAS", 76543321, "ECOSAS@GMAIL.COM");
    
    Proveedor provguardado = new Proveedor(1, "123123123-9", "ECOCOSAS", 76543321, "ECOSAS@GMAIL.COM");
    
    when(proveedorRepository.save(prov)).thenReturn(provguardado);

    Proveedor resultado = proveedorService.save(prov);

    assertThat(resultado.getIdProveedor()).isEqualTo(1);
    assertThat(resultado.getNombProveedor()).isEqualTo("ECOCOSAS");
    assertThat(resultado.getRut()).isEqualTo("123123123-9");
    assertThat(resultado.getTelefono()).isEqualTo(76543321);
    assertThat(resultado.getEmail()).isEqualTo("ECOSAS@GMAIL.COM");
    verify(proveedorRepository).save(prov);


    }

    @Test
    void testListarProveedores() {
    Proveedor prov1 = new Proveedor(1, "123123123-9", "ECOSAS", 123456789, "ECOSAS@GMAIL.COM");
    Proveedor prov2 = new Proveedor(2, "321321321-8", "JardineriaTulipan", 433211233, "JardineriaTulipan@gmail.com");
   
    when(proveedorRepository.findAll()).thenReturn(Arrays.asList(prov1, prov2));

    List<Proveedor> resultado = proveedorService.getAllProveedores();
    assertThat(resultado).hasSize(2).contains(prov1, prov2); //voy a comprobar que el tamaño de la lista sea 2 y que contengan esas dos mascotas
    verify(proveedorRepository).findAll();
    }

    @Test
    void testBuscarProveedorXId(){
        int id = 1;
        Proveedor prove = new Proveedor(id, "123123123-9", "ECOSAS", 123456789, "ECOSAS@GMAIL.COM");
        
        when(proveedorRepository.findById(id)).thenReturn(prove);

        Proveedor resultado = proveedorService.getProveedorById(id);
        assertThat(resultado).isNotNull();
        verify(proveedorRepository).findById(id);
    }

    @Test
    void testBuscarProveedorXRut(){
        String rut = "123123123-9";
        Proveedor prove = new Proveedor(1, rut, "ECOSAS", 123456789, "ECOSAS@GMAIL.COM");

        when(proveedorRepository.findByRut(rut)).thenReturn(prove);

        Proveedor resultado = proveedorService.getProveedorByRut(rut);
        assertThat(resultado).isNotNull();
        verify(proveedorRepository).findByRut(rut);

    }



   /*  @Test
    void testModificarProveedor(){

        int id = 1;
        Proveedor prov = new Proveedor(id, "123123123-9", "ECOSAS", 123456789, "ECOSAS@GMAIL.COM");
        Proveedor provActualizado = new Proveedor(id, "123123123-9", "ECOCOSAS", 123456789, "ECOCOSAS@GMAIL.COM" );
        
        when(proveedorRepository.findBy)
            
    }*/

    @Test
    void testEliminarProveedor() {
    int id = 1;

    doNothing().when(proveedorRepository).deleteById(id);

    proveedorService.delete(id);

    verify(proveedorRepository).deleteById(id);
}


    


}
