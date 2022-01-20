package com.example.demo.repositorios;

import com.example.demo.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador, String>{
    
    @Query("SELECT c FROM Administrador c WHERE c.mail = :mail") // Es la query o consulta en la dase de datos
    public Administrador buscarAdministradorPorMail(@Param("mail") String mail);
    
}
