package com.example.demo;

import com.example.demo.servicios.AdministradorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter {

//    Instancia del servicio administrador para buscar administradores por nombre de administrador
    @Autowired
    public AdministradorServicio administradorServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
             .userDetailsService(administradorServicio) // Configuración del manejador de seguridad del manejador de spring security al cual le decimos cual es el servicio que debe usar para autentificar un administrador
             .passwordEncoder(new BCryptPasswordEncoder()); // Al encontrar al administrador usa el encoder para comparar las contraseñas (encriptadas) y ver que sean iguales
    }
    
    @Override
     protected void configure(HttpSecurity http) throws Exception {
         http.headers().frameOptions().sameOrigin().and()
             .authorizeRequests()
                 .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll() // hasta acá establece que los recursos que estén en css, js o imagen (lo que está en static) los pueda acceder cualquier usuario sin estar logueado
             .and().formLogin()
                 .loginPage("/login") // Que formulario esta mi login
                     .loginProcessingUrl("/logincheck")
                     .usernameParameter("username") // Como viajan los datos del logueo
                     .passwordParameter("password")// Como viajan los datos del logueo
                     .defaultSuccessUrl("/inicio") // A que URL viaja si el usuario se autenticó con éxito
                     .permitAll()
                 .and().logout() // Acá configuro la salida
                     .logoutUrl("/logout")
                     .logoutSuccessUrl("/login?logout")
                     .permitAll().and().csrf().disable();
    }
}
