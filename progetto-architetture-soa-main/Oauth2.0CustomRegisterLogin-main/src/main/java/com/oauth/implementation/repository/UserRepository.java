package com.oauth.implementation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oauth.implementation.model.UserDb;


@Repository
public interface UserRepository extends JpaRepository<UserDb, Integer> {

    UserDb findByEmail(String emailId);
}
