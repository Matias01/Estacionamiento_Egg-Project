package com.example.demo.controlador;

import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.servicios.AdministradorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller // Indica que es un componente de tipo controlador
@RequestMapping("/") // indica la url que va a escuchar el controlados; lo va a escuchar a partir de la barra "/"
public class portalcontrolador {
    
    @Autowired
    private AdministradorServicio administradorServicio;
    
    @GetMapping("/") // Cuando se ingrese a la "/" en el servidor se va a ejecutar el método debajo escrito que es index y va a mostrar el index.html
    public String index(){
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR_REGISTRADO')") // Esto es para que nadie pueda ingresar a la página sin permisos, salvo que esté registrado y se haya logueado con sus datos.
    @GetMapping("/inicio") // Cuando se ingrese a la "/" en el servidor se va a ejecutar el método debajo escrito que es index y va a mostrar el index.html
    public String inicio(){
        return "inicio.html";
    }
    
    //    @RequestParam indica que son parámetros de la solicitud http que se va a efectuar cuando se ejecute el formulario (los nombres son iguales al del html); false indica que no es obligatorio ( opcional)
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model){
        if (error != null) {
            model.put("error", "Nombre de usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente de la plataforma.");
        }
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }
    
//    ModelMap contiene el modelo; sirve para insertar la info que vamos a mostrar en pantalla o vamos a usar en la interface usuario
//    @RequestParam indica que son parámetros de la solicitud http que se va a efectuar cuando se ejecute el formulario (los nombres de los atributos son iguales al del html); false indica que no es obligatorio ( opcional)
    @PostMapping("/registrar") // Responde al método POST del html
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam MultipartFile archivo, @RequestParam Long tel, @RequestParam String clave1, @RequestParam String clave2){
        
        try {
            administradorServicio.registrar(archivo, nombre, apellido, mail, tel, clave1, clave2);
        } catch (ErrorServicio ex) {
            
            modelo.put("error", ex.getMessage());
            return "registro.html";
        }
        
//        titulo y desctripcion son variables del html que muestran los mensajes cargados
        modelo.put("titulo", "Bienvenido/a al Estacionamiento");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    }
    
}
