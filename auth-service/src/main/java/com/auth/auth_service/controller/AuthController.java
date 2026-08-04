package com.auth.auth_service.controller;

import com.auth.auth_service.dtos.AuthResponse;
import com.auth.auth_service.dtos.RegisterMessage;
import com.auth.auth_service.dtos.UserDto;
import com.auth.auth_service.model.UserData;
import com.auth.auth_service.service.AuthService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterMessage> registerUser(@RequestBody UserDto userData) {
        try {
            String res = authService.createUser(userData);
            return ResponseEntity.ok(new RegisterMessage(res));
        } catch(Exception ex) {
            return ResponseEntity.internalServerError().body(new RegisterMessage(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserDto userData) {
        try {
            String res = authService.loginAndGenerateToken(userData);
            return ResponseEntity.ok(new AuthResponse("ok", res));
        } catch(Exception ex) {
           return ResponseEntity.internalServerError().body(new AuthResponse(ex.getMessage(), null));
         }
    }
}
