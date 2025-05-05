package main.ripasso_spring.config;

import main.ripasso_spring.model.entity.User;
import main.ripasso_spring.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// This class is a configuration class, it defines beans that will be managed by Spring
@Configuration
public class DatabaseInitializer {

    // This method will be executed after the application starts
    // It will initialize the database with some test users
    // The UserRepository and PasswordEncoder are injected into this method
    // The CommandLineRunner interface is used to execute code after the application
    // starts
    // The UserRepository is used to save the users to the database

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create test users if they don't exist
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User user = new User();
                user.setName("Regular User");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user123"));
                userRepository.save(user);
            }

            if (userRepository.findByEmail("test@example.com").isEmpty()) {
                User test = new User();
                test.setName("Test User");
                test.setEmail("test@example.com");
                test.setPassword(passwordEncoder.encode("test123"));
                userRepository.save(test);
            }
        };
    }
}