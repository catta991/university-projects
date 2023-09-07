package com.example.userauth;



import com.example.userauth.domain.Role;
import com.example.userauth.domain.User;
import com.example.userauth.userservice.RoleService;
import com.example.userauth.userservice.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


@SpringBootApplication
@RefreshScope
public class UserAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthApplication.class, args);


	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


	//@Bean
	/*CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {

			Role admin = new Role(null, "ROLE_ADMIN", new ArrayList<>());
			Role user = new Role(null, "ROLE_USER", new ArrayList<>());
			Role superAdmin = new Role(null, "ROLE_SUPER_ADMIN", new ArrayList<>());
			roleService.saveRole(admin);
			roleService.saveRole(user);
			roleService.saveRole(superAdmin);

			userService.saveUser(new User(null, "Super Admin", "provasu", "super@admin.it", "superadmin", superAdmin));
			userService.saveUser(new User(null, "Antonio parisi", "anto00", "antonio.parisi@gmail.com", "123456789A", admin));


		};

	}*/

}


















