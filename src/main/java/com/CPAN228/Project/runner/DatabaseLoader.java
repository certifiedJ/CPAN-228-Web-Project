package com.CPAN228.Project.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.CPAN228.Project.model.Clothes;
import com.CPAN228.Project.data.ClothesRepository;
import com.CPAN228.Project.data.UserRepository;
import com.CPAN228.Project.model.User;

@Configuration
public class DatabaseLoader {
    @Bean
     public CommandLineRunner loadData(ClothesRepository clothesRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Add sample clothes
            clothesRepository.save(new Clothes(null, "Cotton T-Shirt", "Nike", 2022, 29.99));
            clothesRepository.save(new Clothes(null, "Running Shorts", "Adidas", 2022, 39.99));
            clothesRepository.save(new Clothes(null, "Winter Jacket", "North Face", 2023, 199.99));
            clothesRepository.save(new Clothes(null, "Denim Jeans", "Levi's", 2022, 79.99));
            clothesRepository.save(new Clothes(null, "Summer Dress", "H&M", 2023, 59.99));
            clothesRepository.save(new Clothes(null, "Wool Sweater", "Gap", 2022, 69.99));
            clothesRepository.save(new Clothes(null, "Pleated Skirt", "Zara", 2021, 49.99));
            clothesRepository.save(new Clothes(null, "Fleece Hoodie", "Nike", 2022, 89.99));

            // Add default users
            userRepository.save(new User(null, "admin", passwordEncoder.encode("admin"), "ADMIN"));
            userRepository.save(new User(null, "w", passwordEncoder.encode("w"), "WAREHOUSE_EMPLOYEE"));
            userRepository.save(new User(null, "r", passwordEncoder.encode("r"), "REGULAR_USER"));
      }; }
}
