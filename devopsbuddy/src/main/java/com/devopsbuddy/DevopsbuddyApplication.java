package com.devopsbuddy;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.PlanService;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;

@SpringBootApplication
public class DevopsbuddyApplication implements CommandLineRunner {

	
	
	private static final Logger log = LoggerFactory.getLogger(DevopsbuddyApplication.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private PlanService planService;
	
	
	@Value("${webmaster.username}")
	private String webmasterUsername;
	
	@Value("${webmaster.password}")
	private String webmasterPassword;

	@Value("${webmaster.email}")
	private String webmasterEmail;
	
	
	public static void main(String[] args) {
		SpringApplication.run(DevopsbuddyApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		
		log.info("Cerating Basic and Pro plans in the database....");
		planService.createPlan(PlansEnum.BASIC.getId());
		planService.createPlan(PlansEnum.PRO.getId());

		
		User user = UserUtils.createUser(webmasterUsername, webmasterEmail);
		user.setPassword(webmasterPassword);
		
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, new Role(RolesEnum.ADMIN)));
		log.debug("Creating user with username {}", user.getUsername());
		userService.createUser(user, PlansEnum.PRO, userRoles);
		log.info("user {} created", user.getUsername());
	}
}
