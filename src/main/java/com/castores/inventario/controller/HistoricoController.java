package com.castores.inventario.controller;

import com.castores.inventario.model.entity.TipoMovimientoEnum;
import com.castores.inventario.service.HistoricoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public String visualizarHistorico(
            @RequestParam(value = "productoId", required = false) Long productoId,
            @RequestParam(value = "tipo", required = false) TipoMovimientoEnum tipo,
            Model model,
            HttpSession session) {

        List<String> permisos = (List<String>) session.getAttribute("permisos");

        if (permisos == null || !permisos.contains("VER_HISTORICO")) {
            return "redirect:/inventario?error=Acceso+denegado";
        }

        if (tipo != null) {
            model.addAttribute("movimientos",
                    historicoService.filtrarPorTipo(tipo));
        } else if (productoId != null) {
            model.addAttribute("movimientos",
                    historicoService.filtrarPorProducto(productoId));
        } else {
            model.addAttribute("movimientos",
                    historicoService.obtenerTodoElHistorico());
        }

        return "historico";
    }
}
