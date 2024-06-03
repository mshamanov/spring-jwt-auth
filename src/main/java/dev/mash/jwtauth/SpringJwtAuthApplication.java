package dev.mash.jwtauth;

import dev.mash.jwtauth.repository.UserRepository;
import dev.mash.jwtauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringJwtAuthApplication {

    private final UserRepository repository;
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(SpringJwtAuthApplication.class, args);
    }
}
