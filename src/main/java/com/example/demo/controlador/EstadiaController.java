package com.example.demo.controlador;
import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.entidades.Estadia;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.servicios.ClienteServicio;
import com.example.demo.servicios.EstablecimientoServicio;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.example.demo.servicios.EstadiaServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
@Controller
@RequestMapping("/estadia")
public class EstadiaController {
    
    @Autowired
    private EstadiaServicio estadiaServicio;
    
    @Autowired
    private EstablecimientoServicio establecimientoServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
//    Modificado
    @GetMapping("/ingresar-estadia")
    public String ingresarEstadia(HttpSession session, @RequestParam(required = false) String eid, @RequestParam(required = false) String cid, @RequestParam(required = false) String idEstadia, @RequestParam(required = false) String accion, ModelMap model){
        
        String lleno = null;
        
        if (accion == null) {
            accion = "Ingresar";
        }
        
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        Estadia estadia = new Estadia();
        Establecimiento establecimiento = new Establecimiento();
        Cliente cliente = new Cliente();
        
        if (idEstadia != null && !idEstadia.isEmpty()) {
            try {
                estadia = estadiaServicio.buscarPorId(idEstadia);
            } catch (ErrorServicio ex) {
                Logger.getLogger(EstablecimientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            establecimiento = establecimientoServicio.buscarPorId(eid);
            cliente = clienteServicio.buscarPorId(cid);
            if (establecimiento.getCantLugaresTotal()<=establecimiento.getCantLugaresOcupados() && "Ingresar".equals(accion)) {
                lleno = "El establecimiento se encuentra lleno";
            } else {
                lleno = null;
            }
        } catch (ErrorServicio ex) {
            Logger.getLogger(EstadiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        model.put("lleno", lleno);
        model.put("idEstadia", idEstadia);
        model.put("cid", cid);
        model.put("eid", eid);
        model.put("establecimiento", establecimiento);
        model.put("cliente", cliente);
        model.put("perfil", estadia);
        model.put("accion", accion);

        return "regEstadia.html";
    }
    
//    Modificado
    @PostMapping("/actualizar")
    public String actualizar(ModelMap modelo, HttpSession session, @RequestParam String eid, @RequestParam String cid, @RequestParam String idEstadia){

        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        try {
            if (idEstadia == null || idEstadia.isEmpty()) {
                estadiaServicio.agregar(eid, cid);
            } else {
                estadiaServicio.modificar(eid, cid, idEstadia);
            }
            return "redirect:/establecimiento/mis-establecimientos";
        } catch (ErrorServicio e) {
            
            Estadia estadia = new Estadia();
            Establecimiento establecimiento = new Establecimiento();
            Cliente cliente = new Cliente();
            
            try {
                establecimiento = establecimientoServicio.buscarPorId(eid);
                cliente = clienteServicio.buscarPorId(cid);
            } catch (ErrorServicio ex) {
                Logger.getLogger(EstadiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            estadia.setId(idEstadia);
            estadia.setEstablecimiento(establecimiento);
            estadia.setCliente(cliente);
            
            modelo.put("accion", "Actualizar");
            modelo.put("error", e.getMessage());
            modelo.put("perfil", estadia);
            modelo.put("nombreEstablecimiento", estadia.getEstablecimiento().getNombre());
            modelo.put("nombreCliente", estadia.getCliente().getNombre());
            modelo.put("idEstadia", idEstadia);
            modelo.put("cid", cid);
            modelo.put("eid", eid);
            
            return "regEstadia.html";
        }
    }
    
    @PostMapping("/retirar")
    public String retirar(HttpSession session, @RequestParam String eid, @RequestParam String idEstadia){

        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        try {
            estadiaServicio.retirar(eid, idEstadia);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EstadiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return "redirect:/estadia/mi-pago?idEstadia=" + idEstadia;
    }
    
    @GetMapping("/mis-estadias")
    public String misEstadias(HttpSession session, ModelMap model, @RequestParam String idEstablecimiento) {
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        List<Estadia> estadias = estadiaServicio.buscarEstadiasPorEstablecimiento(idEstablecimiento);
        model.put("estadias", estadias);
        
        return "estadia";
    }
    
//    Método agregado
    @GetMapping("/mi-pago")
    public String miPago(HttpSession session, ModelMap model, @RequestParam String idEstadia) {
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }
        
        Estadia estadia = new Estadia();
        try {
            estadia = estadiaServicio.buscarPorId(idEstadia);
        } catch (ErrorServicio ex) {
            Logger.getLogger(EstadiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        model.put("estadia", estadia);
        
        return "pago.html";
    }
    
    
    
}
