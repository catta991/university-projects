package com.oauth.implementation.model;


import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // chiave primaria il cui valore viene generato automaticamente
    private int role_id;

    private String role;


    public int getId() {
        return role_id;
    }

    public void setId(int id) {
        this.role_id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
