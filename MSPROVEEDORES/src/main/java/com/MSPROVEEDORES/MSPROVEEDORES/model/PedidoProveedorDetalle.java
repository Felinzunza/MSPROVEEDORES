package com.MSPROVEEDORES.MSPROVEEDORES.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido_proveedor_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDetalle {

@Id
@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
private int idDetalle;

@Column(nullable = false)
private int idproducto;

@Column(nullable = false)
private int cantidad;

}

