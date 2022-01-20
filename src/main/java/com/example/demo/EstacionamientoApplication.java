package com.example.demo;

import com.example.demo.servicios.AdministradorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EstacionamientoApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdministradorServicio administradorServicio;

    public static void main(String[] args) {
            SpringApplication.run(EstacionamientoApplication.class, args);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(administradorServicio).passwordEncoder(new BCryptPasswordEncoder());
    }
    
}
