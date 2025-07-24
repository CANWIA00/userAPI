package org.blaze.userapi.service;

import org.blaze.userapi.auth.AuthUtil;
import org.blaze.userapi.config.JwtService;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.User;
import org.blaze.userapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    protected User findUserById(UUID id){
        return userRepository.findById(id).orElseThrow(()-> new CustomException("User can not find with id!"));
    }

    protected User findUser(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User can not found exception with email:" + username);
        }
        return user;
    }

    protected User getUserByToken(){
        return findUser(AuthUtil.getUsernameByToken());
    }

    protected String getUserFromToken(String token){
        return jwtService.extractUsername(token);
    }

    public User findUserByProfileId(UUID id){
        return userRepository.findUserByProfileId(id);
    }
}
