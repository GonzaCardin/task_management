package com.gonza.task_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gonza.task_management.model.entity.Role;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.repository.RoleRepository;
import com.gonza.task_management.repository.UserRepository;

@Component
public class DataInit implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("USER");
            roleRepository.save(user);
        }

        if (userRepository.count() == 0) {
            User user = new User();
            user.setEmail("admin@example.com");
            user.setPassword(passwordEncoder.encode("admin"));

            Role defaultRole = roleRepository.findByName("ADMIN").get();
            user.addRole(defaultRole);

            userRepository.save(user);
        }

    }

}
