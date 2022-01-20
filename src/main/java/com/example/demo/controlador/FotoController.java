package com.example.demo.controlador;

import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.servicios.AdministradorServicio;
import com.example.demo.servicios.EstablecimientoServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    @Autowired
    private AdministradorServicio administradorServicio;
    
    @Autowired
    private EstablecimientoServicio establecimientoServicio;
    
//    El método devuelve la foto vinculada a un perfil del admin
//    ResponseEntity<byte[]> : posee el contenido de la foto en un arreglo de bytes
    
//    @GetMapping("/administrador/{id}") pasamos el id como parte de la url, por ello usamos @PathVariable en lugar de @RequestParam al solicitar el id
    @GetMapping("/administrador/{id}")
    public ResponseEntity<byte[]> fotoAdministrador(@PathVariable String id) {
        try {
            Administrador administrador = administradorServicio.buscarPorId(id);
            if (administrador.getFoto() == null) {
                throw new ErrorServicio("El administrador no tiene una foto asignada");
            }
            
//            Las fotos se consumen como una URL y eso lo traigo con el .getContenido()
            byte[] foto = administrador.getFoto().getContenido();
            
//            Indicamos en el encabezado que el contenido es del tipo foto (JPEG)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
//            Devolvemos el contenido, la cabecera y el código 200 (HttpStatus.OK) que indica que se ejecutó de manera correcta 
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/establecimiento/{id}")
    public ResponseEntity<byte[]> fotoEstablecimiento(@PathVariable String id) {
        try {
            Establecimiento establecimiento = establecimientoServicio.buscarPorId(id);
            if (establecimiento.getFoto()== null) {
                throw new ErrorServicio("El establecimiento no tiene una foto asignada");
            }
            
            byte[] foto = establecimiento.getFoto().getContenido();
            
//            Indicamos en el encabezado que el contenido es del tipo foto (JPEG)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
//            Devolvemos el contenido, la cabecera y el código 200 (HttpStatus.OK) que indica que se ejecutó de manera correcta 
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
