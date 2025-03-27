package org.blaze.userapi.auth;

import org.blaze.userapi.config.JwtService;
import org.blaze.userapi.exception.UserAlreadyExistsException;
import org.blaze.userapi.model.*;
import org.blaze.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authenticationManager;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(@RequestBody RegisterRequest registerRequest){

        if(findUserByEmail(registerRequest.getEmail()) != null){
            throw new UserAlreadyExistsException("User already exists with this email: " + registerRequest.getEmail());
        }

        var user = new User(
                null,
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                LocalDateTime.now(),
                null,
                new ArrayList<UserSession>(),
                new ArrayList<BlockedUser>(),
                new ArrayList<BlockedUser>(),
                Role.USER
        );

        userRepository.save(user);
        logger.info("User registered successfully!!! :::: " + user);


        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }


    public AuthResponse login(LoginRequest loginRequest) {


        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) {
            throw new UsernameNotFoundException("User not found! " + loginRequest.getEmail());
        }

        if(auth.isAuthenticated()) {
            String token = jwtService.generateToken(user);
            return new AuthResponse(token);
        }else{
            throw new UsernameNotFoundException("User authentication error! " + loginRequest.getEmail());
        }

    }



    protected User findUserByEmail(String mail) {
        return userRepository.findByEmail(mail);
    }
}
