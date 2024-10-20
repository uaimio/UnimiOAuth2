package com.oauth2.authorizationserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oauth2.authorizationserver.model.User;
import com.oauth2.authorizationserver.model.dto.UserDTO;
import com.oauth2.authorizationserver.repository.UserRepository;
import com.oauth2.authorizationserver.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return null;
        return new UserDTO(user);
    }
    
}
