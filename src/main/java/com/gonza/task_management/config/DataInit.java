package com.gonza.task_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.gonza.task_management.model.entity.Role;
import com.gonza.task_management.repository.RoleRepository;

@Component
public class DataInit implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    // @Autowired
    // private UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        
        if(roleRepository.count() == 0){
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("USER");
            roleRepository.save(user);
        }

    }

}
