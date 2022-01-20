package com.example.demo.controlador;

import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.entidades.Estadia;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.servicios.ClienteServicio;
import com.example.demo.servicios.EstablecimientoServicio;
import com.example.demo.servicios.EstadiaServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
@Controller
@RequestMapping("/establecimiento")
public class EstablecimientoController {
    
    @Autowired
    private EstablecimientoServicio establecimientoServicio;
    
    @Autowired
    private EstadiaServicio estadiaServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {
        try {
            Administrador login = (Administrador) session.getAttribute("administradorsession");
            establecimientoServicio.eliminar(login.getId(), id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EstablecimientoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/establecimiento/mis-establecimientos";
    }
    
    @GetMapping("/mis-establecimientos")
    public String misEstablecimientos(HttpSession session, ModelMap model) {
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        List<Establecimiento> establecimientos = establecimientoServicio.buscarEstablecimientosPorAdministrador(login.getId());
        model.put("establecimiento", establecimientos);
        
        return "establecimiento";
    }
   
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

        if (accion == null) {
            accion = "Crear";
        }
        
//        Necesitamos el id del administrador logueado en la sesión
//        Con esto, de la 67 a la 70, resolvemos un bug de administrador cuando se inicia la sesión
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }

        Establecimiento establecimiento = new Establecimiento();

//        Le asigno el establecimiento según su id
        if (id != null && !id.isEmpty()) {
            try {
                establecimiento = establecimientoServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(EstablecimientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        model.put("perfil", establecimiento);
        model.put("accion", accion);

        return "regEstab";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Integer cantLugaresTotal) {

        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/inicio";
        }
        
        try {
//            Si el id es null o vacío vamos a crear un establecimiento, sino solo la editamos
            if (id == null || id.isEmpty()) {
                establecimientoServicio.agregarEstablecimiento(archivo, login.getId(), nombre, cantLugaresTotal);
            } else {
                establecimientoServicio.modificar(archivo, login.getId(), id, nombre, cantLugaresTotal);
            }
            return "redirect:/establecimiento/mis-establecimientos";
        } catch (ErrorServicio e) {

            Establecimiento establecimiento = new Establecimiento();
            establecimiento.setId(id);
            establecimiento.setNombre(nombre);
            establecimiento.setCantLugaresTotal(cantLugaresTotal);

            modelo.put("accion", "Actualizar");
            modelo.put("error", e.getMessage());
            modelo.put("perfil", establecimiento);

            return "regEstab";
        }
    }
    
//    Modificado
    @GetMapping("/ingresoEstadia")
    public String ingresoEstadia(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam(required = false) Long dni) throws ErrorServicio{
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/inicio";
        }
        
        Establecimiento establecimiento = establecimientoServicio.buscarPorId(id);
        List<Estadia> estadia = estadiaServicio.buscarEstadiasPorEstablecimiento(id);
        List<Cliente> cliente = clienteServicio.buscarClientesPorId();
        
        if (dni != null) {
            Cliente clienteDni = clienteServicio.buscarPorDni(dni);
            modelo.put("cliente", clienteDni);
        } else {
            modelo.put("cliente", cliente);
        }
        
        modelo.put("idEstablecimiento", id);
        modelo.put("estadia", estadia);
        modelo.put("establecimiento", establecimiento);
        
        return "estadia.html";
    }
    
    
}
