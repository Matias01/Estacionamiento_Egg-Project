package com.example.demo.servicios;

import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.entidades.Foto;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.AdministradorRepositorio;
import com.example.demo.repositorios.EstablecimientoRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EstablecimientoServicio {
    
    @Autowired
    private AdministradorRepositorio administradorRepositorio;
    
    @Autowired
    private EstablecimientoRepositorio establecimientoRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void agregarEstablecimiento(MultipartFile archivo, String idAdministrador, String nombre, Integer lugaresTotales) throws ErrorServicio {
        
        Administrador administrador = administradorRepositorio.findById(idAdministrador).get();
        
        validar(nombre, lugaresTotales);
        
        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setNombre(nombre);
        establecimiento.setCantLugaresTotal(lugaresTotales);
        establecimiento.setCantLugaresOcupados(0);
        establecimiento.setCantLugaresLibres(lugaresTotales-establecimiento.getCantLugaresOcupados());
        establecimiento.setAlta(true);
        establecimiento.setAdministrador(administrador);
        
        Foto foto = fotoServicio.guardar(archivo);
        establecimiento.setFoto(foto);
        
        establecimientoRepositorio.save(establecimiento);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo, String idAdministrador, String idEstablecimiento, String nombre, Integer lugaresTotales) throws  ErrorServicio {
        
        validar(nombre, lugaresTotales);
        
        Optional<Establecimiento> respuesta = establecimientoRepositorio.findById(idEstablecimiento);
        if (respuesta.isPresent()) {
            Establecimiento establecimiento = respuesta.get();
            if (establecimiento.getAdministrador().getId().equals(idAdministrador)) { // Identifica al usuario (dueño) de la mascota
                establecimiento.setNombre(nombre);
                establecimiento.setCantLugaresTotal(lugaresTotales);
                establecimiento.setCantLugaresOcupados(establecimiento.getCantLugaresOcupados());
                establecimiento.setCantLugaresLibres(lugaresTotales-establecimiento.getCantLugaresOcupados());
                
                String idFoto = null;
                if (establecimiento.getFoto()!= null) {
                    idFoto = establecimiento.getFoto().getId();
                }
                
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                establecimiento.setFoto(foto);
                
                establecimientoRepositorio.save(establecimiento);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes para realizar la operación");
            }
        } else {
            throw new ErrorServicio("No existe un establecimiento con el identificador solicitado");
        }
    }
    
    @Transactional
    public void eliminar(String idAdministrador, String idEstablecimiento) throws ErrorServicio {
        Optional<Establecimiento> respuesta = establecimientoRepositorio.findById(idEstablecimiento);
        if (respuesta.isPresent()) {
            Establecimiento establecimiento = respuesta.get();
            if (establecimiento.getAdministrador().getId().equals(idAdministrador)) { // Identifica al usuario (dueño) de la mascota
                establecimiento.setAlta(false);
                establecimientoRepositorio.save(establecimiento);
            }
        } else {
            throw new ErrorServicio("No existe un establecimiento con el identificador solicitado");
        }
    }
    
    private void validar(String nombre, Integer lugaresTotales) throws ErrorServicio {
        if (nombre==null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del establecimiento no debe ser nulo");
        }
        if (lugaresTotales==null) {
            throw new ErrorServicio("La cantidad de lugares totales no debe ser nula");
        }
    }
    
    public Establecimiento buscarPorId(String id) throws ErrorServicio{
        Optional<Establecimiento> respuesta = establecimientoRepositorio.findById(id);
        if(respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El establecimiento solicitado no existe.");
        }
    }
    
    public List<Establecimiento> buscarEstablecimientosPorAdministrador(String id) {
        return establecimientoRepositorio.buscarEstablecimientosPorAdministrador(id);
    }
    
//    Agregué este método
    public String buscarIdEstablecimientoPorIdAdministrador(String id) {
        return establecimientoRepositorio.buscarIdEstablecimientoPorAdministrador(id);
    }
}
