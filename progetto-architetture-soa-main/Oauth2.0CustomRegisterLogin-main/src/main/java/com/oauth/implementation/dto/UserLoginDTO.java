package com.oauth.implementation.dto;

import lombok.ToString;

@ToString

// classe di supporto per salvare le informazioni ottenute dal form di login
public class UserLoginDTO {

    private String username;

    private String password;

    private int otp;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }


}
