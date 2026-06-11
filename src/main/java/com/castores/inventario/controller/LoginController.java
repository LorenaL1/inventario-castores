package com.castores.inventario.controller;

import com.castores.inventario.model.entity.Permiso;
import com.castores.inventario.model.entity.Usuario;
import com.castores.inventario.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByCorreoAndContrasena(username, password)
                .orElse(null);

        if (usuario == null) {
            model.addAttribute("error",
                    "Credenciales incorrectas.");
            return "login";
        }

        session.setAttribute("idUsuario", usuario.getIdUsuario());

        session.setAttribute("usuario", usuario.getNombre());
 
        session.setAttribute("rol", usuario.getRol().getNombreRol());

        session.setAttribute("permisos", usuario.getRol().getPermisos());

        List<String> permisos = usuario.getRol()
                .getPermisos()
                .stream()
                .map(Permiso::getNombrePermiso)
                .toList();

        session.setAttribute("permisos", permisos);

        return "redirect:/inventario";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?error=Sesion+cerrada+correctamente.";
    }
}
