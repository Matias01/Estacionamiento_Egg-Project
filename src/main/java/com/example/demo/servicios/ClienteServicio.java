package com.example.demo.servicios;

import com.example.demo.entidades.Cliente;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.ClienteRepositorio;
import com.example.demo.repositorios.EstadiaRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {
    
    @Autowired
    private EstadiaRepositorio estadiaRepositorio;
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Transactional
    public void agregarCliente(String nombre, String apellido, String mail, Long dni, Long tel) throws ErrorServicio {
        Cliente cliente = new Cliente();
//        if (!clienteRepositorio.buscarClientePorMail(mail).getMail().equals(mail) || clienteRepositorio.buscarClientePorMail(mail).equals("")) {
            validar(nombre, apellido, mail, dni, tel);
        
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setMail(mail);
            cliente.setDni(dni);
            cliente.setTel(tel);
            cliente.setAlta(true);
            clienteRepositorio.save(cliente);
           
//        } else {
//            throw new ErrorServicio("El email ya se encuentra registrado");
//        }
        
    }
    
    @Transactional
    public void modificar(String idCliente, String nombre, String apellido, String mail, Long dni, Long tel) throws  ErrorServicio {
        
        validar(nombre, apellido, mail, dni, tel);
        
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
//            if (!clienteRepositorio.buscarClientePorMail(mail).getMail().equals(mail) || clienteRepositorio.buscarClientePorMail(mail).equals("")) {
                
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setDni(dni);
                cliente.setMail(mail);
                cliente.setTel(tel);
                clienteRepositorio.save(cliente);
            } else {
                throw new ErrorServicio("El email ya se encuentra registrado");
            }
            
//        } else {
//            throw new ErrorServicio("No existe un cliente con el identificador solicitado");
//        }
    }
    
    @Transactional
    public void eliminar(String idCliente) throws  ErrorServicio {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(false);
            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No existe un cliente con el identificador solicitado");
        }
    }
    
    private void validar(String nombre, String apellido, String mail, Long dni, Long tel) throws ErrorServicio {
        if (nombre==null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del cliente no debe ser nulo");
        }
        if (apellido==null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del cliente no debe ser nulo");
        }
        if (mail==null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del cliente no debe ser nulo");
        }
        if (dni==null || dni.toString().length() < 5) {
            throw new ErrorServicio("El dni del cliente no debe ser nulo");
        }
        if (tel==null || tel.toString().length() <= 6 || tel.toString().length() >= 12) {
            throw new ErrorServicio("El tel del cliente no debe ser nulo");
        }
//        if (!clienteRepositorio.buscarClientePorMail(mail).getMail().equals(mail)) {
//            throw new ErrorServicio("El email ya se encuentra en uso");
//        }
        
    }
    
    public Cliente buscarPorId(String id) throws ErrorServicio{
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if(respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El cliente no existe.");
        }
    }
    
    public Cliente buscarPorDni(Long dni) throws ErrorServicio{
        return clienteRepositorio.buscarClientePorDNI(dni);
    }
    
    public List<Cliente> buscarClientesPorId() throws ErrorServicio{
        return clienteRepositorio.findAll();
    }
    
}
