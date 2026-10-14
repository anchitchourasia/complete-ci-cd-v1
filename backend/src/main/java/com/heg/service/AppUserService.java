package com.heg.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.heg.dto.ChangePasswordRequest;
import com.heg.dto.RegisterRequest;
import com.heg.model.AppUser;
import com.heg.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {

        List<AppUser> existingUsers =
                repository.findUsersByUsername(
                        request.getUsername()
                );

        if (!existingUsers.isEmpty()) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        AppUser user = new AppUser();

        user.setUsername(
                request.getUsername()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(
                request.getRole().toUpperCase()
        );

        user.setStatus("ACTIVE");

        repository.save(user);

        return "User registered successfully";
    }

    public AppUser getUserByUsername(
            String username) {

        List<AppUser> users =
                repository.findUsersByUsername(username);

        if (users.isEmpty()) {

            throw new RuntimeException(
                    "User not found"
            );
        }

        return users.get(0);
    }

    public String changePassword(
            String username,
            ChangePasswordRequest request) {

        AppUser user =
                getUserByUsername(username);

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Old password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        repository.save(user);

        return "Password changed successfully";
    }

    public String updateStatus(
            String username,
            String status) {

        AppUser user =
                getUserByUsername(username);

        String userStatus =
                status.toUpperCase();

        if (!userStatus.equals("ACTIVE")
                && !userStatus.equals("INACTIVE")) {

            throw new RuntimeException(
                    "Status must be ACTIVE or INACTIVE"
            );
        }

        user.setStatus(userStatus);

        repository.save(user);

        return "User status updated successfully";
    }

    public List<AppUser> getAllUsers() {

        return repository.findAll();
    }
}