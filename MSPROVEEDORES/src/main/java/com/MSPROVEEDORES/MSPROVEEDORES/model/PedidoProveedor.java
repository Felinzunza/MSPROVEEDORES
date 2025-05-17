package com.MSPROVEEDORES.MSPROVEEDORES.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(nullable = true)
    private int idTienda; //se obtendra por rest desde microservicio inventario

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaRecepcionEsperada;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPedProFk") //se crea esta columna como FK en la tabla detalle
    private List<PedidoProveedorDetalle> detallePedidoProveedor;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnumEstado estado;
    
    @ManyToOne
    @JoinColumn(name = "idProveedor") //se crea esta columna como FK en la tabla pedido_proveedor
    @JsonIdentityReference(alwaysAsId = true) //evita bucle infinito al serializar y me devuelve solo el id, no el cuerpo json
    private Proveedor proveedor;


}
