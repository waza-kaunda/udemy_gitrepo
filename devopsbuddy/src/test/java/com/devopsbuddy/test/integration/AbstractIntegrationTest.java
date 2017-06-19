package com.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;

public abstract class AbstractIntegrationTest {

	@Autowired
	protected PlanRepository planRepository;
	@Autowired
	protected RoleRepository roleRepository;
	@Autowired
	protected UserRepository userRepository;
	
	
	protected Plan createPlan(PlansEnum planEnum) {	
		return new Plan(planEnum);
	}
	
	
	protected Role createRole(RolesEnum rolesEnum) {		
		return new Role(rolesEnum);
	}
	
	
	protected User createNewUser(String username, String email) {
	
		
		Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		
		User user = UserUtils.createUser(username, email);
		user.setPlan(basicPlan);
		
		Role basicRole = createRole(RolesEnum.BASIC);
		roleRepository.save(basicRole);
		
		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole(user, basicRole);
		userRoles.add(userRole);
		
		user.getUserRoles().addAll(userRoles);
		user = userRepository.save(user);
		
		return user;
	}
	
	protected User createUser(TestName testName){
		return createNewUser(testName.getMethodName(), testName.getMethodName() + "@devopssbuddy.com");
	}

}