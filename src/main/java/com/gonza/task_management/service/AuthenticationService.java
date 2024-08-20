package com.gonza.task_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gonza.task_management.model.dto.AuthenticationResponse;
import com.gonza.task_management.model.dto.UserRequest;
import com.gonza.task_management.model.entity.Role;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.repository.RoleRepository;
import com.gonza.task_management.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.addRole(defaultRole);

        userRepository.save(user);

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtService.getToken(user));
        return authResponse;
    }

    public AuthenticationResponse authenticate(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtService.getToken(user));
        return authResponse;
    }
}
