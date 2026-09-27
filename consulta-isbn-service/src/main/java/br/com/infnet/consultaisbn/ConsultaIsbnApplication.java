package br.com.infnet.consultaisbn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConsultaIsbnApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ConsultaIsbnApplication.class, args);
    }
}
