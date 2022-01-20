package com.example.demo.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Establecimiento {
    
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    private String nombre;
    private Integer cantLugaresTotal;
    private Integer cantLugaresOcupados;
    private Integer cantLugaresLibres;
    private Boolean alta;
    
    @ManyToOne
    private Administrador administrador;
    
    @OneToOne
    private Foto foto;

    public Establecimiento() {
    }

    public Establecimiento(String id, String nombre, Integer cantLugaresTotal, Integer cantLugaresOcupados, Integer cantLugaresLibres, Boolean alta, Administrador administrador, Foto foto) {
        this.id = id;
        this.nombre = nombre;
        this.cantLugaresTotal = cantLugaresTotal;
        this.cantLugaresOcupados = cantLugaresOcupados;
        this.cantLugaresLibres = cantLugaresLibres;
        this.alta = alta;
        this.administrador = administrador;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantLugaresTotal() {
        return cantLugaresTotal;
    }

    public void setCantLugaresTotal(Integer cantLugaresTotal) {
        this.cantLugaresTotal = cantLugaresTotal;
    }

    public Integer getCantLugaresOcupados() {
        return cantLugaresOcupados;
    }

    public void setCantLugaresOcupados(Integer cantLugaresOcupados) {
        this.cantLugaresOcupados = cantLugaresOcupados;
    }

    public Integer getCantLugaresLibres() {
        return cantLugaresLibres;
    }

    public void setCantLugaresLibres(Integer cantLugaresLibres) {
        this.cantLugaresLibres = cantLugaresLibres;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Establecimiento{" + "id=" + id + ", nombre=" + nombre + ", cantLugaresTotal=" + cantLugaresTotal + ", cantLugaresOcupados=" + cantLugaresOcupados + ", cantLugaresLibres=" + cantLugaresLibres + ", alta=" + alta + ", administrador=" + administrador + ", foto=" + foto + '}';
    }
    
}
