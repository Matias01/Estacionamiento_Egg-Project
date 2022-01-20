package com.example.demo.servicios;

import com.example.demo.entidades.Administrador;
import com.example.demo.entidades.Foto;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.AdministradorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdministradorServicio implements UserDetailsService {
    
    @Autowired // El servidor inicializa esa variable, no tenemos que hacerlo nosotros
    private AdministradorRepositorio administradorRepositorio;
    
//    @Autowired
//    private NotificacionServicio notificacionServicio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional // Si el método se ejecuta sin lanzar una excepción se hace un commit y se aplican todos los cambios ; cualquier excepcion hace un rollback y se vuelve atrás
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, Long tel, String clave, String clave2) throws ErrorServicio {
        
        validar(nombre, apellido, mail, tel, clave, clave2);
        
        Administrador administrador = new Administrador();
//        if (!administradorRepositorio.buscarAdministradorPorMail(mail).getMail().equals(mail) || administradorRepositorio.buscarAdministradorPorMail(mail).equals("")) {
            administrador.setNombre(nombre);
            administrador.setApellido(apellido);
            administrador.setMail(mail);
            administrador.setTel(tel);
            
//        Persistimos con la clave encriptada con el mismo método que usa spring
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            administrador.setClave(encriptada);

            administrador.setAlta(true);

            Foto foto = fotoServicio.guardar(archivo);
            administrador.setFoto(foto);
//        } else { // Si no lo encuentra dispara la excepción
//            throw new ErrorServicio("El email ya se encuentra registrado");
//        }
        administradorRepositorio.save(administrador); // Recibimos los datos de tipo usuario y los almacena en MySQL
        
//        notificacionServicio.enviar("Bienvenidos al Tinder de Mascotas", "Tinder de Mascotas", usuario.getMail());
    }
    
    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, Long tel, String clave, String clave2) throws ErrorServicio {
        
        validar(nombre, apellido, mail, tel, clave, clave2);
        
//        Optional: Es en caso que el id no sea correcto y el usuario no exista
        Optional<Administrador> respuesta = administradorRepositorio.findById(id); // Busca el usuario por el id
        if (respuesta.isPresent()) {
            Administrador administrador = respuesta.get();
//            if (!administradorRepositorio.buscarAdministradorPorMail(mail).getMail().equals(mail) || administradorRepositorio.buscarAdministradorPorMail(mail).equals("")) {
//             El id se autogenera con el uuid, por lo cual no es necesario incluirlo
            
            administrador.setNombre(nombre);
            administrador.setApellido(apellido);
            administrador.setMail(mail);
            administrador.setTel(tel);

//            Persistimos con la clave encriptada
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            administrador.setClave(encriptada);

            String idFoto = null;
            if (administrador.getFoto() != null) {
                idFoto = administrador.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            administrador.setFoto(foto);

            administradorRepositorio.save(administrador);
//            } else {
//                throw new ErrorServicio("El email ya se encuentra registrado");
//            }
        } else { // Si no lo encuentra dispara la excepción
            throw new ErrorServicio("No se encontró el administrador solicitado");
        }
        
    }
    
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
                
        Optional<Administrador> respuesta = administradorRepositorio.findById(id); // Busca el usuario por el id
        if (respuesta.isPresent()) {
//        El id se autogenera con el uuid, por lo cual no es necesario incluirlo
        Administrador administrador = respuesta.get();
        administrador.setAlta(false);
        
        administradorRepositorio.save(administrador);
        
        } else { // Si no lo encuentra dispara la excepción
            throw new ErrorServicio("No se encontró el administrador solicitado");
        }
        
    }
    
    @Transactional
    public void habilitar(String id) throws ErrorServicio {

        Optional<Administrador> respuesta = administradorRepositorio.findById(id); // Busca el usuario por el id
        if (respuesta.isPresent()) {
//        El id se autogenera con el uuid, por lo cual no es necesario incluirlo
            Administrador administrador = respuesta.get();
//            Se borra la fecha de baja
            administrador.setAlta(true);

            administradorRepositorio.save(administrador);

        } else { // Si no lo encuentra dispara la excepción
            throw new ErrorServicio("No se encontró el administrador solicitado");
        }

    }
    
    private void validar(String nombre, String apellido, String mail, Long tel, String clave, String clave2) throws ErrorServicio {
//        if(administradorRepositorio.buscarAdministradorPorMail(mail).getMail().equals(mail)){
//            throw new ErrorServicio("El email ya se encuentra en uso");
//        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del administrador no debe ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del administrador no debe ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del administrador no debe ser nulo");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("El clave del administrador no debe ser nula y debe tener más de 6 dígitos");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
        if (tel==null) {
            throw new ErrorServicio("El teléfono no puede ser nulo.");
        }
    }
    
    //    Busca un usuario de nuestro dominio y lo transforma en un usuario del dominio de spring security
    @Override // Crea permisos de usuario según el mail
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException { // Método de spring security para asignarle permisos si el email existe
        Administrador administrador = administradorRepositorio.buscarAdministradorPorMail(mail);
        if (administrador != null) {
//            Nos da el listado de permisos del usuario
            List<GrantedAuthority> permisos = new ArrayList<>();
            
//            El usuario autenticado tiene el rol ROLE_USUARIO_REGISTRADO, esto valida que pueda acceder al método o el controlador
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_ADMINISTRADOR_REGISTRADO");
            permisos.add(p1);
            
//            Llamada para guardar el usuario y recupera los datos de la sesión del usuario logueado (el request de la solicitud http) 
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
//            En la variable session usuariosession se guarda el objeto con todos los datos del usuario logueado
            session.setAttribute("administradorsession", administrador);
            
            User user = new User(administrador.getMail(), administrador.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
    
    public Administrador buscarPorId(String id) throws ErrorServicio{
        Optional<Administrador> respuesta = administradorRepositorio.findById(id);
        if(respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El administrador solicitado no existe.");
        }
    }
}
