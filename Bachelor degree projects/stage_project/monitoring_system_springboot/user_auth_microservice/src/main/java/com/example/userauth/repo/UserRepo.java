package com.example.userauth.repo;

import com.example.userauth.domain.User;
import com.example.userauth.dto.SingleUserDto;
import com.example.userauth.util.UpdateUserUtil;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
