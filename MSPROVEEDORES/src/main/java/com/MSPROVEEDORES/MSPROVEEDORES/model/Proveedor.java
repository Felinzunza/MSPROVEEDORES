package com.MSPROVEEDORES.MSPROVEEDORES.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProveedor;
    
    @Column(length = 12, unique = true, nullable = false)
    private String rut;

    @Column(length = 50, nullable = false)
    private String nombProveedor;
    
    @Column(nullable = false)
    private int telefono;

    @Column(length = 50, nullable = false)
    private String email;
    
    



}
