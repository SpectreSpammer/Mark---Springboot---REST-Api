package com.onepieceofjava.SpringRestApiDemo.security.init;

import com.onepieceofjava.SpringRestApiDemo.security.model.User;
import com.onepieceofjava.SpringRestApiDemo.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //if admin doesnt exist
        if(userRepository.findByUsername("admin").isEmpty()){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ADMIN"));
            userRepository.save(admin);
            System.out.println("Admin user is created");
        }

        //if regular user doesnt exist
        if(userRepository.findByUsername("user").isEmpty()){
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(Set.of("USER"));
            userRepository.save(user);
            System.out.println("Regular user is created");
        }
    }
}
