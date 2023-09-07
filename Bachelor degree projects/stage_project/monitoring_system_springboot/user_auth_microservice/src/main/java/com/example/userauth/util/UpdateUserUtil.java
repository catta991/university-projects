package com.example.userauth.util;

import com.example.userauth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateUserUtil {

    private String username;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String roleName;


}
