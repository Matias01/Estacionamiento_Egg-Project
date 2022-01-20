package com.example.demo.servicios;

import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Establecimiento;
import com.example.demo.entidades.Estadia;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.ClienteRepositorio;
import com.example.demo.repositorios.EstablecimientoRepositorio;
import com.example.demo.repositorios.EstadiaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadiaServicio {
      
    @Autowired
    private EstablecimientoRepositorio establecimientoRepositorio;
    
    @Autowired
    private EstablecimientoServicio establecimientoServicio;

    @Autowired 
    private EstadiaRepositorio estadiaRepositorio;
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Transactional
    public void agregar(String idEstablecimiento, String idCliente) throws ErrorServicio { //fecha de salida no se setea, queda nula, entonces el lugar está ocupado
        
        if (establecimientoServicio.buscarPorId(idEstablecimiento).getCantLugaresTotal()>establecimientoServicio.buscarPorId(idEstablecimiento).getCantLugaresOcupados()) {
            validar(idEstablecimiento, idCliente);
        
            Establecimiento establecimiento = establecimientoRepositorio.findById(idEstablecimiento).get();
            Cliente cliente = clienteRepositorio.findById(idCliente).get();

            establecimiento.setCantLugaresOcupados(establecimiento.getCantLugaresOcupados()+1);
            establecimiento.setCantLugaresLibres(establecimiento.getCantLugaresTotal()-establecimiento.getCantLugaresOcupados());

            Estadia estadia = new Estadia();
            estadia.setCliente(cliente);
            estadia.setEstablecimiento(establecimiento);
            estadia.setAlta(true); //??
            estadia.setFechaEntrada(new Date());

            estadiaRepositorio.save(estadia);
        } else {
            throw new ErrorServicio("El establecimiento se encuentra lleno");
        }
        
    }
    
    @Transactional
    public void modificar(String idEstablecimiento, String idCliente, String idEstadia) throws  ErrorServicio { // Date fechaEntrada, Date fechaSalida
        
        validar(idEstablecimiento, idCliente);
       
        Optional<Estadia> respuesta = estadiaRepositorio.findById(idEstadia);
        Cliente cliente = clienteRepositorio.findById(idCliente).get();
        
        if (respuesta.isPresent()) {
            if (establecimientoServicio.buscarPorId(idEstablecimiento).getCantLugaresTotal()>establecimientoServicio.buscarPorId(idEstablecimiento).getCantLugaresOcupados()) {
                Estadia estadia = respuesta.get();
                if (estadia.getEstablecimiento().getId().equals(idEstablecimiento)) { // Identifica al establecimiento de la estadía

                    estadia.setCliente(cliente);
                    estadiaRepositorio.save(estadia);
                } else {
                    throw new ErrorServicio("No está en el establecimiento correcto");
                }
            } else {
                throw new ErrorServicio("El establecimiento se encuentra lleno");
            }
        } else {
            throw new ErrorServicio("No existe la estadia con ese identificador");
        }
    }
    
    @Transactional
    public void retirar(String idEstablecimiento, String idEstadia) throws  ErrorServicio { // Date fechaEntrada, Date fechaSalida

        Optional<Estadia> respuesta = estadiaRepositorio.findById(idEstadia);
        Optional<Establecimiento> respuesta2 = establecimientoRepositorio.findById(idEstablecimiento);
        
        if (respuesta.isPresent() && respuesta2.isPresent()) {
            Establecimiento establecimiento = respuesta2.get();
            Estadia estadia = respuesta.get();
            if (estadia.getEstablecimiento().getId().equals(idEstablecimiento)) { // Identifica al establecimiento de la estadía
                estadia.setFechaSalida(new Date());
                establecimiento.setCantLugaresOcupados(establecimiento.getCantLugaresOcupados()-1);
                establecimiento.setCantLugaresLibres(establecimiento.getCantLugaresTotal()-establecimiento.getCantLugaresOcupados());
                estadia.setPago(pagar(estadia.getFechaEntrada(), estadia.getFechaSalida()));
                
                estadiaRepositorio.save(estadia);
//            } else {
//                throw new ErrorServicio("No tiene permisos suficientes para modificar la estadia");
            }
        } else {
            throw new ErrorServicio("No existe una estadia con ese identificador");
        }
    }

    private void validar(String idEstablecimiento, String idCliente) throws ErrorServicio {
        if (idEstablecimiento==null || idEstablecimiento.isEmpty()) {
            throw new ErrorServicio("El establecimiento no puede ser nulo");
        }
        if (idCliente==null || idCliente.isEmpty()) {
            throw new ErrorServicio("El cliente no puede ser nulo");
        }
     }
    
    private Double pagar(Date fechaEntrada, Date fechaSalida) {
        
        double base = 5;
        double dia = fechaSalida.getDay()-fechaEntrada.getDay();
        double hs = fechaSalida.getHours()-fechaEntrada.getHours();
        double min = fechaSalida.getMinutes()-fechaEntrada.getMinutes();
        
        return (dia*24*60 + hs*60 + min) * base;
    }
    
    public Estadia buscarPorId(String id) throws ErrorServicio{
        Optional<Estadia> respuesta = estadiaRepositorio.findById(id);
        if(respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("La estadía no existe.");
        }
    }
    
    public List<Estadia> buscarEstadiasPorEstablecimiento(String id) {
        return estadiaRepositorio.buscarEstadiasPorEstablecimiento(id);
    }
    
}
