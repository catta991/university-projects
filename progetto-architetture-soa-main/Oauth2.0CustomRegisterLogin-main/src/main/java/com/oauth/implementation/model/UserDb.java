package com.oauth.implementation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString

public class UserDb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // chiave primaria il cui valore viene generato automaticamente
    private int id;

    private String name;

    private String email;

    private String password;


    // indica una relazione molti a uno con la relazione ruolo dove l'inizializzazione avviene immediatamente (fect=EAGER)
    @ManyToOne(fetch = EAGER)
    // l'attributo di join in cui viene memorizzata la chiave esterna si chiama role_id
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private Role role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
