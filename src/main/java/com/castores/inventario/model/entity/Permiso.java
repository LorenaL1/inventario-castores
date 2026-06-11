package com.castores.inventario.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "permisos")
@Data
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPermiso")
    private Long idPermiso;

    @Column(name = "nombrePermiso")
    private String nombrePermiso;

    @Column(name = "descripcion")
    private String descripcion;

}
