package com.devopsbuddy.backend.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;

@Service
@Transactional(readOnly = true)
public class UserService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	/**
	 * The application logger
	 */	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	
	/**
	 * Creates a new user with a plan and roles
	 * @param user
	 * @param planEnum
	 * @param userRoles
	 * @return
	 */
	@Transactional
	public User createUser(User user, PlansEnum planEnum, Set<UserRole> userRoles){
		
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		
		Plan plan = new Plan(planEnum);
		
		// make sure plans exist in the database
		if(!planRepository.exists(planEnum.getId())){
			plan = planRepository.save(plan);
		}
		
		user.setPlan(plan);
		
		for (UserRole ur : userRoles) {
			roleRepository.save(ur.getRole());
		}
		
		user.getUserRoles().addAll(userRoles);
		
		user = userRepository.save(user);
				
		return user;		
		
	}
	
	/**
	 * Updates a given users password
	 * @param userId
	 * @param password
	 */
	@Transactional
	public void updateUserPassword(long userId, String password){
		password = passwordEncoder.encode(password);
		userRepository.updateUserPassword(userId, password);
		log.debug("Password updated successfully for user id {} ", userId);		
	}
	
	/**
	 * Retrieves a user by id
	 * @param userId
	 * @return
	 */
	@Transactional
	public User findById(long userId){
		return userRepository.findOne(userId);
	}

}
