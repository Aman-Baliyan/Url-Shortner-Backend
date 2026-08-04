package com.auth.auth_service.service;

import com.auth.auth_service.dtos.UserDto;
import com.auth.auth_service.model.UserData;
import com.auth.auth_service.repository.UserRepo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtService jwtService;

    public String createUser(UserDto userData) {
        if (userRepo.findByEmail(userData.email()).isPresent()) {
            throw new RuntimeException("User Already Existed");
        }
        String pass = bCryptPasswordEncoder.encode(userData.password());

        userRepo.save(new UserData(userData.email(), pass, "ROLE_USER"));
        return "User Created";
    }

    public String loginAndGenerateToken(UserDto userData) {
        System.out.println(userData.email());
        UserData userData1 = userRepo.findByEmail(userData.email()).orElse(null);
        if (userData1 == null) {
            throw new RuntimeException("User not existed");
        }
        boolean passwordMatches = bCryptPasswordEncoder.matches(userData.password(), userData1.getPassword());
        if (passwordMatches) {
            return jwtService.generateToken(
                    userData1.getEmail(),
                    String.valueOf(userData1.getUser_id()),
                    Arrays.stream(userData1.getRoles().split(",")).toList()
            );
        }
        else {
            throw new RuntimeException("Password mismatch");
        }

    }
}
