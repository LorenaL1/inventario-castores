package com.castores.inventario.controller;

import com.castores.inventario.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/salidas")
public class SalidaController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String mostrarModuloSalidas(Model model,
                                       HttpSession session) {

        List<String> permisos =
                (List<String>) session.getAttribute("permisos");

        if (permisos == null ||
                !permisos.contains("VER_SALIDAS")) {

            return "redirect:/inventario";
        }

        model.addAttribute(
                "productos",
                productoService.obtenerProductosActivos()
        );

        return "salidas";
    }

    @PostMapping("/procesar")
    public String procesarSalida(@RequestParam("idProducto") Long id,
                                 @RequestParam("cantidad") int cantidad,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        List<String> permisos = (List<String>) session.getAttribute("permisos");
        if (permisos == null || !permisos.contains("SACAR_INVENTARIO_ALAMACEN")) {
            redirectAttributes.addFlashAttribute("error", "No autorizado.");
            return "redirect:/inventario";
        }

        try {
            Long idUsuario = (Long) session.getAttribute("idUsuario");

            productoService.disminuirInventario(
                    id,
                    cantidad,
                    idUsuario
            );
            redirectAttributes.addFlashAttribute("success", "Salida de material autorizada y registrada.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/salidas";
    }
}
