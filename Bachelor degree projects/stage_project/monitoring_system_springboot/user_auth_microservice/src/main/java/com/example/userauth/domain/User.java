package com.example.userauth.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String username;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private String password;
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private Role role;


    public User(String name, String username, String email, String password, Role role) {

        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public Collection<Role> getRoleCollection(){
        Collection <Role> coll = new ArrayList<>();
        coll.add(this.role);
        return  coll;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", role="
                + role + "]";
    }


}
