package com.example.demo.repositorios;

import com.example.demo.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.dni = :dni") // Es la query o consulta en la dase de datos
    public Cliente buscarClientePorDNI(@Param("dni") Long dni);
    
    @Query("SELECT c FROM Cliente c WHERE c.id = :id") // Es la query o consulta en la dase de datos
    public List<Cliente> buscarClientesPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Cliente c WHERE c.mail = :mail") // Es la query o consulta en la dase de datos
    public Cliente buscarClientePorMail(@Param("mail") String mail);
}