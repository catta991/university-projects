package com.oauth.implementation.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.model.UserDb;


public interface DefaultUserService extends UserDetailsService {

    UserDb save(UserRegisteredDTO userRegisteredDTO);

    UserDb findUser(String name);


}
