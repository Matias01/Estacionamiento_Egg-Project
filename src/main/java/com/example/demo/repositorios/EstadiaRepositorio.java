package com.example.demo.repositorios;

import com.example.demo.entidades.Estadia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaRepositorio extends JpaRepository<Estadia, String>{
    
    @Query("SELECT c FROM Estadia c WHERE c.establecimiento.id = :id AND c.fechaSalida IS NULL") // Es la query o consulta en la dase de datos
    public List<Estadia> buscarEstadiasPorEstablecimiento(@Param("id") String id);
    
}
