package com.MSPROVEEDORES.MSPROVEEDORES.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido_proveedor")
public class PedidoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedidoProveedor;
    
    private int idProveedor;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private EnumEstado estado;
}
