package com.castores.inventario.controller;

import com.castores.inventario.model.entity.Producto;
import com.castores.inventario.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventario")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    private boolean tienePermiso(HttpSession session, String permisoRequerido){
        List<String> permisos = (List<String>) session.getAttribute("permisos");
        return permisos != null && permisos.contains(permisoRequerido);

    }

    // 1. VER MÓDULO INVENTARIO (Accesible por Administrador y Almacenista)
    @GetMapping
    public String listarInventario(Model model, HttpSession session) {
        if (!tienePermiso(session, "VER_MODULO_INVENTARIO")) {
            return "redirect:/login?error=No+tienes+permiso+para+ver+el+inventario";
        }

        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "inventario";
    }

    // 2. AGREGAR NUEVO PRODUCTO (Solo Administrador)
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute Producto producto, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!tienePermiso(session, "AGREGAR_NUEVOS_PRODUCTO")) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado: No tienes el permiso AGREGAR_PRODUCTO");
            return "redirect:/inventario";
        }

        productoService.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("success", "Producto registrado exitosamente con stock inicial 0.");
        return "redirect:/inventario";
    }

    // 3. AUMENTAR INVENTARIO (Solo Administrador)
    @PostMapping("/aumentar")
    public String aumentarInventario(@RequestParam("idProducto") Long id,
                                     @RequestParam("cantidad") int cantidad,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!tienePermiso(session, "AUMENTAR_INVENTARIO")) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado.");
            return "redirect:/inventario";
        }

        try {
            Long idUsuario = (Long) session.getAttribute("idUsuario");
            productoService.aumentarInventario(
                    id,
                    cantidad,
                    idUsuario
            );
            redirectAttributes.addFlashAttribute("success", "Inventario aumentado correctamente.");
        } catch (IllegalArgumentException e) {
            // REQUERIMIENTO: "Si intentas disminuir la cantidad... mostrará un mensaje de error."
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    // 4. DAR DE BAJA / REACTIVAR (Solo Administrador)
    @GetMapping("/cambiar-estatus/{id}")
    public String cambiarEstatus(@PathVariable("id") Long id,
                                 @RequestParam("estatus") boolean estatus,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!tienePermiso(session, "BAJA_REACTIVAR_PRODUCTO")) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado.");
            return "redirect:/inventario";
        }

        productoService.cambiarEstatus(id, estatus);
        String mensaje = estatus ? "Producto reactivado correctamente." : "Producto dado de baja de forma lógica.";
        redirectAttributes.addFlashAttribute("success", mensaje);
        return "redirect:/inventario";
    }
}
