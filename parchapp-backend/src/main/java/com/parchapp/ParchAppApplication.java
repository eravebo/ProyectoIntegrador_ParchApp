package com.parchapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ParchAppApplication
 *
 * Clase principal de arranque de la aplicación Spring Boot.
 * @SpringBootApplication activa tres cosas:
 *   - @Configuration:       permite definir beans en esta clase
 *   - @EnableAutoConfiguration: configura automáticamente Spring según las dependencias
 *   - @ComponentScan:       escanea los paquetes hijos buscando componentes (@Service, @Controller, etc.)
 *
 * Para ejecutar:
 *   mvn spring-boot:run
 *   o correr esta clase como Java Application desde el IDE
 */
@SpringBootApplication
public class ParchAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParchAppApplication.class, args);
    }
}
