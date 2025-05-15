package com.MSPROVEEDORES.MSPROVEEDORES.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "pedido_proveedor")
public class PedidoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedidoProveedor;

    /*@Column(nullable = false)
    private int idTienda;*/ //De Microservicio Inventario

    @Column(nullable = false)
    private int idProveedor;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaRecepcionEsperada;

    @Column(nullable = false)
    private EnumEstado estado;
    
    @ManyToOne
    @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor", insertable = false, updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Proveedor proveedor;
 
    /*private List<Integer>idsproductos;*/ //De Microservicio Inventario
}
