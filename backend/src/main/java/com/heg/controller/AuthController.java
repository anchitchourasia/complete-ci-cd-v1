package com.heg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heg.dto.ChangePasswordRequest;
import com.heg.dto.LoginRequest;
import com.heg.dto.LoginResponse;
import com.heg.dto.RegisterRequest;
import com.heg.model.AppUser;
import com.heg.security.JwtService;
import com.heg.service.AppUserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AppUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	
    
    public AuthController(AppUserService userService, AuthenticationManager authenticationManager,
			JwtService jwtService) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}


	@PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        String response =
                userService.register(request);

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generateToken(userDetails);

        AppUser user =
                userService.getUserByUsername(
                        request.getUsername()
                );

        LoginResponse response =
                new LoginResponse(
                        token,
                        user.getUsername(),
                        user.getRole()
                );

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid
            @RequestBody ChangePasswordRequest request) {

        String username =
                authentication.getName();

        return ResponseEntity.ok(
                userService.changePassword(
                        username,
                        request
                )
        );
    }
}
