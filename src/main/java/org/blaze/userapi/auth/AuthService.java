package org.blaze.userapi.auth;

import org.blaze.userapi.config.JwtService;
import org.blaze.userapi.model.*;
import org.blaze.userapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(@RequestBody RegisterRequest registerRequest){
        var user = new User(
                null,
                registerRequest.getMail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                LocalDateTime.now(),
                null,
                new ArrayList<UserSession>(null),
                new ArrayList<Friend>(null),
                new ArrayList<Friend>(null),
                new ArrayList<BlockedUser>(null),
                new ArrayList<BlockedUser>(null),
                Role.USER
        );

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
