package com.gonza.task_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.gonza.task_management.model.dto.AuthenticationResponse;
import com.gonza.task_management.model.dto.Response;
import com.gonza.task_management.model.dto.UserRequest;
import com.gonza.task_management.model.entity.User;

@Service
public class AuthenticationService {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequest userRequest) {
        Response<User> userResoponse = userService.createUser(userRequest);
        User user = userResoponse.getData();

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

        User user = userService.getUserByEmail(userRequest.getEmail());
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtService.getToken(user));
        return authResponse;
    }
}
