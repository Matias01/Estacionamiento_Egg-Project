package com.example.demo.controlador;

import com.example.demo.entidades.Administrador;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.servicios.AdministradorServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    
    @Autowired
    private AdministradorServicio administradorServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

//        Recupero el usuario de la sesión cuando se loguea, lo casteo y lo guardo en login.
        Administrador login = (Administrador) session.getAttribute("administradorsession");
//        Si el login es nulo o el id es distinto al id del usuario logueado no debe estar ahí, por eso lo redirecciono al inicio 
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Administrador administrador = administradorServicio.buscarPorId(id);
            model.addAttribute("perfil", administrador);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "perfilA.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam Long tel, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2) {
        Administrador administrador = null;
        try {
//        Recupero el usuario de la sesión cuando se loguea, lo casteo y lo guardo en login.
            Administrador login = (Administrador) session.getAttribute("administradorsession");
//        Si el login es nulo o el id es distinto al id del usuario logueado no debe estar ahí, por eso lo redirecciono al inicio 
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            
            administrador = administradorServicio.buscarPorId(id);
            administradorServicio.modificar(archivo, id, nombre, apellido, mail, tel, clave1, clave2);
            session.setAttribute("administradorsession", administrador);
            return "redirect:/inicio";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("perfil", administrador);
            
            return "perfilA.html";
        }
    }
}
