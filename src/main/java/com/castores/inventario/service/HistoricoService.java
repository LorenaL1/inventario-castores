package com.castores.inventario.service;

import com.castores.inventario.model.entity.Historico;
import com.castores.inventario.model.entity.TipoMovimientoEnum;
import com.castores.inventario.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoService {
    @Autowired
    private HistoricoRepository historicoRepository;

    public List<Historico> obtenerTodoElHistorico() {
        return historicoRepository.findAll();
    }

    public List<Historico> filtrarPorProducto(Long productoId) {
        return historicoRepository.findByProductoIdProducto(productoId);
    }

    public List<Historico> filtrarPorTipo(TipoMovimientoEnum tipo) {
        return historicoRepository.findByTipo(tipo);
    }

    public Historico guardarMovimiento(Historico historico) {
        return historicoRepository.save(historico);
    }
}
