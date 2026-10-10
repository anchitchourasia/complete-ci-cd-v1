package com.heg.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.heg.model.AppUser;
import com.heg.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    public CustomUserDetailsService(
            AppUserRepository repository) {

        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        List<AppUser> users =
                repository.findUsersByUsername(username);

        if (users.isEmpty()) {

            throw new UsernameNotFoundException(
                    "User not found: " + username
            );
        }

        AppUser user = users.get(0);

        boolean active =
                "ACTIVE".equalsIgnoreCase(
                        user.getStatus()
                );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!active)
                .build();
    }
}