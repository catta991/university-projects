package com.example.userauth.dto;

// react mi restituisce un json con le informazioni dell'utente creato che comprende anche
// un array contenete i ruoli e un array contenente i gruppi e questa classe
// mi serve per parsare il json ricevuto in input e poi salvare l'utente nel db

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CrudUserDto {
    private String email;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String roleName;
    // quando modifico il nome in react devo modificarlo anche qui
    private String[] contactGroups;
}
