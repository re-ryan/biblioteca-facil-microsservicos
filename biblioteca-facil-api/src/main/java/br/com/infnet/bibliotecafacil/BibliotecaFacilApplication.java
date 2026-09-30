package br.com.infnet.bibliotecafacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
public class BibliotecaFacilApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BibliotecaFacilApplication.class, args);
    }
}
