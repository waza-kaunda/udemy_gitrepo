
package com.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public void init() {
		Assert.assertNotNull(planRepository);
		Assert.assertNotNull(roleRepository);
		Assert.assertNotNull(userRepository);
	}

	@Test
	public void testCreateNewPlan() throws Exception {
		Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
		Assert.assertNotNull(retrievedPlan);
	}

	@Test
	public void testCreateNewRole() throws Exception {
		Role userRole = createRole(RolesEnum.BASIC);
		roleRepository.save(userRole);
		Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
		Assert.assertNotNull(retrievedRole);
	}
	
	@Test
	public void testCreateNewUser() throws Exception {
		
		Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		
		User basicUser = UserUtils.createBasicUser();
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createRole(RolesEnum.BASIC);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole userRole = new UserRole(basicUser, basicRole);
		userRoles.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoles);
		
		for(UserRole ur : userRoles){
			roleRepository.save(ur.getRole());
		}
		
		basicUser = userRepository.save(basicUser);
		User newlyCreatedUser = userRepository.findOne(basicUser.getId());
		
		Assert.assertNotNull(newlyCreatedUser);
		Assert.assertTrue(newlyCreatedUser.getId() != 0);
		Assert.assertNotNull(newlyCreatedUser.getPlan());
		Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
		
		Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.getUserRoles();
		for (UserRole ur : newlyCreatedUserUserRoles) {
			Assert.assertNotNull(ur.getRole());
			Assert.assertNotNull(ur.getRole().getId());
		}
		
	}

	private Plan createPlan(PlansEnum planEnum) {	
		return new Plan(planEnum);
	}

	private Role createRole(RolesEnum rolesEnum) {		
		return new Role(rolesEnum);
	}

}
