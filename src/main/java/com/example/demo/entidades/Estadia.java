package com.example.demo.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Estadia {
    
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    
//    Usamos Date o LocalDate???
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrada;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;
    
    private Boolean alta;
    
    @ManyToOne
    private Establecimiento establecimiento;
    
    @OneToOne
    private Cliente cliente;
    
//   Lo pasamos a factura a posteriori.    
    private Double pago;

    public Estadia() {
    }

    public Estadia(String id, Date fechaEntrada, Date fechaSalida, Boolean alta, Establecimiento establecimiento, Cliente cliente, Double pago) {
        this.id = id;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.alta = alta;
        this.establecimiento = establecimiento;
        this.cliente = cliente;
        this.pago = pago;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Double getPago() {
        return pago;
    }

    public void setPago(Double pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        return "Estadia{" + "id=" + id + ", fechaEntrada=" + fechaEntrada + ", fechaSalida=" + fechaSalida + ", alta=" + alta + ", establecimiento=" + establecimiento + ", cliente=" + cliente + ", pago=" + pago + '}';
    }

}
