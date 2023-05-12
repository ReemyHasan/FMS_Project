package com.example.userManagement;

import com.example.userManagement.entity.RegisterRequest;
import com.example.userManagement.entity.Role;
import com.example.userManagement.services.AuthenticationService;
import com.example.userManagement.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}
//
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service,
//			RoleService roleService
//	) {
//		return args -> {
//
//			var Role1 = new Role("admin");
//			var Role2 = new Role("admin");
//			roleService.insertRole(Role1);
//			roleService.insertRole(Role2);
//			var admin = RegisterRequest.builder()
//					.firstname("admin1")
//					.lastname("admin1")
//					.email("admin1@mail.com")
//					.password("password")
//					.role("admin")
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("admin2")
//					.lastname("admin2")
//					.email("admin2@mail.com")
//					.password("password")
//					.role("admin")
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//		};
//	}
}
