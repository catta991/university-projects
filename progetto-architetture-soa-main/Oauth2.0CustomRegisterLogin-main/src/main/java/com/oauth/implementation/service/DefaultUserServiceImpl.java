package com.oauth.implementation.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oauth.implementation.repository.RoleRepository;
import com.oauth.implementation.repository.UserRepository;
import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.model.Role;
import com.oauth.implementation.model.UserDb;


@Service
public class DefaultUserServiceImpl implements DefaultUserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // metodo obbligatorio per ottenere l'utente dal db per il login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDb user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        // le granthed autority sono degli oggetti dello spring security che servono per far capire ad esso il ruolo dell'utente
        // se è un User o un Admin
        Collection<GrantedAuthority> auth = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().getRole());
        auth.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), auth);
    }


    // query per salvare un nuovo utente
    @Override
    public UserDb save(UserRegisteredDTO userRegisteredDTO) {

        // recupero il ruolo dal database (sia id che nome)
        Role role = roleRepo.findByRole("USER");

        // creo un nuovo oggetto utente
        UserDb user = new UserDb();
        user.setEmail(userRegisteredDTO.getEmail_id());
        user.setName(userRegisteredDTO.getName());

        // per policy dello spring security la password salvata nel db deve essere cifrata
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRole(role);

        // salvo l'utente nel db
        return userRepo.save(user);
    }

    // query per trovare un utente nel db data la mail
    public UserDb findUser(String name) {
        return userRepo.findByEmail(name);
    }

}
