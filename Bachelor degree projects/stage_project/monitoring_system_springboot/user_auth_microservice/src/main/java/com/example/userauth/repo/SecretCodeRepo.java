package com.example.userauth.repo;

import com.example.userauth.domain.SecretCode;
import com.example.userauth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecretCodeRepo extends JpaRepository<SecretCode, Long> {


}
