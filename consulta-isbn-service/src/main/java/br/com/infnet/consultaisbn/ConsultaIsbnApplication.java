package br.com.infnet.consultaisbn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
public class ConsultaIsbnApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ConsultaIsbnApplication.class, args);
    }
}
