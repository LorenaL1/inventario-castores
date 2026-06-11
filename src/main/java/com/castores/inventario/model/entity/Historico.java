package com.castores.inventario.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico")
@Data
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimiento")
    private Long idMovimiento;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoMovimientoEnum tipo;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;
}
