package com.MSPROVEEDORES.MSPROVEEDORES.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
private int idInventario;

@Column(nullable = false)
private int cantidad;

@ManyToOne
@JoinColumn(name = "idPedProFk")
@JsonBackReference
private PedidoProveedor pedidoProveedor;


}

