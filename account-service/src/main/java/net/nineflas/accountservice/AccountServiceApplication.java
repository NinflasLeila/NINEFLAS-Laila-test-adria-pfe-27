package net.nineflas.accountservice;

import net.nineflas.accountservice.service.AccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(AccountService service) {
        return args -> {
            service.create("CUST-001", "MAD", 10000);
            service.create("CUST-002", "MAD", 5000);
            service.create("CUST-003", "EUR", 2500);
        };
    }
}
