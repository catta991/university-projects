package com.example.userauth.userservice;

import com.example.userauth.domain.User;
import com.example.userauth.util.UpdateUserUtil;
import org.springframework.stereotype.Service;


public interface UserService {

    User saveUser(User user);


    User getUser(String username);

    void deleteUser(User user);

    User updateUser(User user);

    User updateUserNoPsw(User user);



}
