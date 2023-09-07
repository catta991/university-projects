package com.example.userauth.userservice;

import com.example.userauth.domain.SecretCode;

import java.util.List;

public interface SecretCodeService {

    List<SecretCode> getSecretCodeEntity();
    SecretCode setEntity();
}
