package com.example.SpringSecurityDemo.Config;

import com.example.SpringSecurityDemo.Entity.User.AppUser;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Repository.AppUserRepository;
import com.example.SpringSecurityDemo.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {


        @Bean
        public CommandLineRunner loadData(AppUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
            return args -> {
                // Save roles if they don't already exist
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_ADMIN")));
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_USER")));
                Role organizerRole = roleRepository.findByName("ROLE_ORGANIZER")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_ORGANIZER"))); // ROLE_ORGANIZER

                // Save Admin user
                if (userRepository.findByUsername("admin").isEmpty()) {
                    AppUser adminUser = new AppUser();
                    adminUser.setName("Admin User");
                    adminUser.setUsername("admin");
                    adminUser.setEmail("admin@example.com");
                    adminUser.setPassword(passwordEncoder.encode("admin123"));

                    adminUser.setRoles(Set.of(adminRole)); // Attach admin role
                    userRepository.save(adminUser);
                }

                // Save Normal User
                if (userRepository.findByUsername("user").isEmpty()) {
                    AppUser normalUser = new AppUser();
                    normalUser.setName("Normal User");
                    normalUser.setUsername("user");
                    normalUser.setEmail("user@example.com");
                    normalUser.setPassword(passwordEncoder.encode("user123"));

                    normalUser.setRoles(Set.of(userRole)); // Attach user role
                    userRepository.save(normalUser);
                }

                // Save Organizer User
                if (userRepository.findByUsername("organizer").isEmpty()) {
                    AppUser organizerUser = new AppUser();
                    organizerUser.setName("Organizer User");
                    organizerUser.setUsername("organizer");
                    organizerUser.setEmail("organizer@example.com");
                    organizerUser.setPassword(passwordEncoder.encode("organizer123"));

                    organizerUser.setRoles(Set.of(organizerRole)); // Attach organizer role
                    userRepository.save(organizerUser);
                }

                System.out.println("Data Loaded Successfully!");
            };
        }




}