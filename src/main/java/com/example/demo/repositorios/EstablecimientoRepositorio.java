package com.example.demo.repositorios;

import com.example.demo.entidades.Establecimiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablecimientoRepositorio extends JpaRepository<Establecimiento, String>{
    
    @Query("SELECT c FROM Establecimiento c WHERE c.administrador.id = :id AND c.alta IS TRUE") // Es la query o consulta en la dase de datos
    public List<Establecimiento> buscarEstablecimientosPorAdministrador(@Param("id") String id);
    
//    Agregué esta query
    @Query("SELECT c.id FROM Establecimiento c WHERE c.administrador.id = :id")
    public String buscarIdEstablecimientoPorAdministrador(@Param("id") String id);
    
}
