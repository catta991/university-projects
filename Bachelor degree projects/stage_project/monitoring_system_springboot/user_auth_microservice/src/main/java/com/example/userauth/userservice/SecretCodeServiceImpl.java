package com.example.userauth.userservice;

import com.example.userauth.domain.SecretCode;
import com.example.userauth.repo.SecretCodeRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@NoArgsConstructor
@Transactional
public class SecretCodeServiceImpl implements SecretCodeService {

    @Autowired
    private SecretCodeRepo secretCodeRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    public void generateSecretCode(String secretCode) {

        List<SecretCode> secret = secretCodeRepo.findAll();

        if (secret.isEmpty()) {
            secretCodeRepo.save(new SecretCode(passwordEncoder.encode(secretCode), true));
        }

    }

    @Override
    public  List<SecretCode> getSecretCodeEntity(){
        return secretCodeRepo.findAll();
    }

    @Override
    public SecretCode setEntity() {
        List<SecretCode> secretCodeList = secretCodeRepo.findAll();
        SecretCode secret = null;

        System.out.println(secretCodeList);

        if(!secretCodeList.isEmpty() && secretCodeList.get(0).getFirstAccess()) {
            secret = secretCodeList.get(0);
            secret.setFirstAccess(false);
        }
        return secret;
    }

}
