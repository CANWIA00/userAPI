package org.blaze.userapi.service;

import org.blaze.userapi.auth.AuthService;
import org.blaze.userapi.auth.AuthUtil;
import org.blaze.userapi.model.User;
import org.blaze.userapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
