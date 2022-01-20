package com.example.demo.servicios;

import com.example.demo.entidades.Foto;
import com.example.demo.excepciones.ErrorServicio;
import com.example.demo.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {
    
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    @Transactional // Si el método se ejecuta sin lanzar una excepción se hace un commit y se aplican todos los cambios ; cualquier excepcion hace un rollback y se vuelve atrás
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
//            Para manejar la excepción del .getByte() que puede tirar un error (lo recomienda Java)
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
            
                return fotoRepositorio.save(foto);
            } catch (IOException e){
//                Si hay error, imprime el mensaje y devuelve una foto nula
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                
                if (idFoto != null) {
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }
                }
                
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                
                return fotoRepositorio.save(foto);
            } catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
