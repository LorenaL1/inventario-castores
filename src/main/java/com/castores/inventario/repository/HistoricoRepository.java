package com.castores.inventario.repository;

import com.castores.inventario.model.entity.Historico;
import com.castores.inventario.model.entity.TipoMovimientoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByProductoIdProducto(Long productoId);

    List<Historico> findByTipo(TipoMovimientoEnum tipo);
}
