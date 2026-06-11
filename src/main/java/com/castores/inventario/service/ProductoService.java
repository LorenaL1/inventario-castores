package com.castores.inventario.service;

import com.castores.inventario.model.entity.Historico;
import com.castores.inventario.model.entity.Producto;
import com.castores.inventario.model.entity.TipoMovimientoEnum;
import com.castores.inventario.model.entity.Usuario;
import com.castores.inventario.repository.HistoricoRepository;
import com.castores.inventario.repository.ProductoRepository;
import com.castores.inventario.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private  ProductoRepository productoRepository;
    @Autowired
    private  HistoricoRepository historicoRepository;
    @Autowired
    private  UsuarioRepository usuarioRepository;

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosActivos(){
        return productoRepository.findByEstatusTrue();
    }

    public Producto guardarProducto(Producto producto){
        producto.setCantidad(0);
        producto.setEstatus(true);
        return productoRepository.save(producto);
    }

    public void aumentarInventario(Long idProducto,
                                   int cantidadAumentar,
                                   Long idUsuario) {

        System.out.println("ENTRO A aumentarInventario");

        if (cantidadAumentar < 0) {
            throw new IllegalArgumentException("Error: Si intentas disminuir la cantidad de inventario actual, mostrará un mensaje de error.");
        }
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setCantidad(producto.getCantidad() + cantidadAumentar);
        productoRepository.save(producto);



        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Historico historico = new Historico();
        historico.setProducto(producto);
        historico.setUsuario(usuario);
        historico.setTipo(TipoMovimientoEnum.ENTRADA);
        historico.setCantidad(cantidadAumentar);
        historico.setFechaHora(LocalDateTime.now());



        System.out.println("Producto: " + producto.getIdProducto());
        System.out.println("Usuario: " + usuario.getIdUsuario());
        System.out.println("Tipo: " + TipoMovimientoEnum.ENTRADA);

        Historico guardado = historicoRepository.save(historico);

        System.out.println("Historico guardado: " + guardado.getIdMovimiento());


        historicoRepository.save(historico);
    }

    public void disminuirInventario(Long idProducto,
                                    int cantidadRestar,
                                    Long idUsuario) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!producto.getEstatus()) {
            throw new IllegalStateException("Error: No se puede sacar inventario de un producto dado de baja.");
        }

        if (cantidadRestar > producto.getCantidad()) {
            throw new IllegalArgumentException("Error: No se puede sacar una cantidad mayor de un producto de la que está en inventario.");
        }

        producto.setCantidad(producto.getCantidad() - cantidadRestar);
        productoRepository.save(producto);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Historico historico = new Historico();
        historico.setProducto(producto);
        historico.setUsuario(usuario);
        historico.setTipo(TipoMovimientoEnum.SALIDA);
        historico.setCantidad(cantidadRestar);
        historico.setFechaHora(LocalDateTime.now());

        historicoRepository.save(historico);
    }

    public void cambiarEstatus(Long id, boolean nuevoEstatus) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstatus(nuevoEstatus);
        productoRepository.save(producto);
    }
}
