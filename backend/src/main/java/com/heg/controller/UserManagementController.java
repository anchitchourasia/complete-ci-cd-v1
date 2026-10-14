package com.heg.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heg.dto.UserStatusRequest;
import com.heg.model.AppUser;
import com.heg.service.AppUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
public class UserManagementController {
	private final AppUserService userService;

    public UserManagementController(
            AppUserService userService) {

        this.userService = userService;
    }

    @PutMapping("/{username}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String username,
            @Valid
            @RequestBody UserStatusRequest request) {

        return ResponseEntity.ok(
                userService.updateStatus(
                        username,
                        request.getStatus()
                )
        );
    }
    
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }
}
