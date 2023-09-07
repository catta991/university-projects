package com.example.userauth.userservice;

import com.example.userauth.domain.Role;
import com.example.userauth.domain.User;
import com.example.userauth.repo.RoleRepo;
import com.example.userauth.repo.UserRepo;
import com.example.userauth.util.UpdateUserUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    // questo metodo viene chiamato per ottenere le info sull'utente dal db che verranno inserite
    // in un oggetto di spring security chiamato UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("in loadUserByUsername");

        // faccio la query per ottenere lo user dal db
        User user = userRepo.findByUsername(username);

        System.out.println("user detail service " + username);

        //System.out.println("user detail service " + user);
        if (user == null) {
            // log.info("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            // log.info("User found in the database: {}", username);
            // converto i ruoli in una collezione di SimpleGrantedAuthority in modo tale
            // che possano essere comprese da spring security
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            Role role = user.getRole();

            System.out.println("user detail service role name " + role.getName());
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            // restituisco l'oggetto User di spring security che contiene username , password e
            // i ruoli
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    @Override
    public User saveUser(User user) {
        //log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }


    @Override
    public User getUser(String username) {
        // log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }


    @Override
    public void deleteUser(User user) {

        userRepo.delete(user);
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User updateUserNoPsw(User user) {
        return userRepo.save(user);
    }

}
