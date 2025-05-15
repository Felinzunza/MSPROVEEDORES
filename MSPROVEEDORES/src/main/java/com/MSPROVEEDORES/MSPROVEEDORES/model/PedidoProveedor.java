package com.MSPROVEEDORES.MSPROVEEDORES.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private int idAlmacen;*/ //De Microservicio Inventario

    @Column(nullable = false)
    private int idProveedor;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaRecepcionEsperada;

    @Column(nullable = false)
    private EnumEstado estado;

 
    /*private List<Integer>idsproductos;*/ //De Microservicio Inventario
}
