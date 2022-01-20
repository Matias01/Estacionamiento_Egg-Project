package com.example.demo.excepciones;

public class ErrorServicio extends Exception{
    //    Lo creamos para diferenciar nuestros errores de los que crea el sistema.
    
    public ErrorServicio(String msj){
        super(msj);
    }
}
