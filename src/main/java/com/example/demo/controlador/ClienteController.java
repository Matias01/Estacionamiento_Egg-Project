package com.example.demo.controlador;

import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.entidades.Estadia;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.ClienteRepositorio;
import com.example.demo.servicios.ClienteServicio;
import java.util.ArrayList;
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

@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')")
@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

//        Agregamos un parámetro llamado accion del HTML del título (para que pueda imprimir Crear o Actualizar)
        if (accion == null) {
            accion = "Registrar";
        }
        
//        Necesitamos el id del Administrador logueado en la sesión
//        Con esto resolvemos un bug del Administrador cuando se inicia la sesión
        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/login";
        }

        Cliente cliente = new Cliente();

//        Le asigno el cliente según su id
        if (id != null && !id.isEmpty()) {
            try {
                cliente = clienteServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        model.put("perfil", cliente);
        model.put("accion", accion);

        return "cliente.html";
    }
    
////    Para una vista para ver todos los clientes según su administrador
//    @GetMapping("/mis-clientes")
//    public String misClientes(ModelMap modelo, HttpSession session){
//        Administrador login = (Administrador) session.getAttribute("administradorsession");
//        if (login == null) {
//            return "redirect:/login";
//        }
//        
//        List<Cliente> clientes = new ArrayList<Cliente>();
//        try {
//            clientes = clienteServicio.buscarClientesPorId();
//        } catch (ErrorServicio ex) {
//            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        modelo.put("clientes", clientes);
//        
//        return "estadia";
//    }

//    @GetMapping("/list")
//    public String buscarCliente(ModelMap modelo, HttpSession session, @RequestParam(required = false) Long dni){
//        Administrador login = (Administrador) session.getAttribute("administradorsession");
//        if (login == null) {
//            return "redirect:/login";
//        }
//        
//        try {
//            if (dni != null) {
//                modelo.put("cliente", clienteServicio.buscarPorDni(dni));
//            } else {
//                modelo.put("cliente", clienteServicio.buscarClientesPorId());
//            }
//        } catch (ErrorServicio ex) {
//            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return "estadia";
//    }
    
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam Long dni, @RequestParam Long tel) {

        Administrador login = (Administrador) session.getAttribute("administradorsession");
        if (login == null) {
            return "redirect:/inicio";
        }

        try {
            if (id == null || id.isEmpty()) {
                clienteServicio.agregarCliente(nombre, apellido, mail, dni, tel);
            } else {
                clienteServicio.modificar(id, nombre, apellido, mail, dni, tel);
            }
            return "redirect:/establecimiento/mis-establecimientos";
        } catch (ErrorServicio e) {

            Cliente cliente = new Cliente();
            cliente.setId(id);
            cliente.setApellido(apellido);
            cliente.setNombre(nombre);
            cliente.setDni(dni);
            cliente.setMail(mail);
            cliente.setTel(tel);

            modelo.put("accion", "Actualizar");
            modelo.put("error", e.getMessage());
            modelo.put("perfil", cliente);

            return "cliente.html";
        }
    }
    
//    @PostMapping("/eliminar-cliente")
//    public String eliminar(HttpSession session, @RequestParam String id){
//
//        Administrador login = (Administrador) session.getAttribute("administradorsession");
//        if (login == null) {
//            return "redirect:/login";
//        }
//        
//        try {
//            clienteServicio.eliminar(id);
//        } catch (ErrorServicio ex) {
//            Logger.getLogger(EstablecimientoController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return "redirect:/establecimiento/ingresoEstadia";
//    }
    
}
