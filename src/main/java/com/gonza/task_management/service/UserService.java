package com.gonza.task_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gonza.task_management.model.dto.Response;
import com.gonza.task_management.model.dto.UserRequest;
import com.gonza.task_management.model.entity.Role;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.repository.RoleRepository;
import com.gonza.task_management.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO: Implement Response to create user
    @Transactional
    public User createUser(UserRequest user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);

        Role defaultRole = roleRepository.findByName("DEVELOPER")
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        newUser.addRole(defaultRole);

        return newUser;
    }

    @Transactional
    public Response<User> updateUser(UserRequest user) {
        Response<User> response = new Response<>();
        try {
            User userToUpdate = getUserByEmail(user.getEmail());

            if (user.getEmail() != null && !user.getEmail().equals(user.getNewEmail())) {
                User existingUser = getUserByEmail(user.getNewEmail());
                if (existingUser != null && !existingUser.getId().equals(userToUpdate.getId())) {
                    response.setSuccess(false);
                    response.setMessage("Email already in use");
                    return response;
                }
                userToUpdate.setEmail(user.getNewEmail());
            }

            if (user.getPassword() != null && !user.getPassword().equals(user.getNewPassword())) {
                userToUpdate.setPassword(passwordEncoder.encode(user.getNewPassword()));
            }

            userRepository.save(userToUpdate);
            response.setSuccess(true);
            response.setMessage("User updated successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error updating user: " + e.getMessage());
            return response;
        }

        return response;
    }

    @Transactional
    public Response<User> addRole(String email, String roleName) {
        Response<User> response = new Response<>();
        try {
            User user = getUserByEmail(email);
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
            user.addRole(role);
            userRepository.save(user);

            response.setSuccess(true);
            response.setMessage("Role added successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error adding role: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public Response<User> removeRole(String email, String roleName) {
        Response<User> response = new Response<>();
        try {
            User user = getUserByEmail(email);
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
            user.removeRole(role);
            userRepository.save(user);

            response.setSuccess(true);
            response.setMessage("Role removed successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error removing role: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public Response<User> deleteUser(String email) {
        Response<User> response = new Response<>();
        try {
            User user = getUserByEmail(email);
            userRepository.delete(user);

            response.setSuccess(true);
            response.setMessage("User deleted successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error deleting user: " + e.getMessage());
        }

        return response;
    }

    @Transactional
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
