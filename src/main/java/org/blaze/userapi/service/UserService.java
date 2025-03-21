package org.blaze.userapi.service;

import org.blaze.userapi.model.User;
import org.blaze.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getUserByToken(String username){
        return userRepository.findByEmail(username);
    }
}
