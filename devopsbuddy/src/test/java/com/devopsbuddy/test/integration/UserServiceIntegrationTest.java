package com.devopsbuddy.test.integration;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.persistence.domain.backend.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Rule public TestName testName = new TestName();
	
	@Before
	public void init(){
		
	}

	@Test
	public void testCreateNewUserService() throws Exception {	
		
		User user = createUser(testName);
		assertNotNull(user);
		assertNotNull(user.getId());
	}
	
	@Test
	public void testUpdateUserPasswordService() throws Exception {
		
		User user = createUser(testName);
		assertNotNull(user);
		assertNotNull(user.getId());
		
		String newPassword = UUID.randomUUID().toString();
		userService.updateUserPassword(user.getId(), newPassword);	
		
		user = userService.findById(user.getId());
		assertNotNull(user);
	}
	
	@Test
	public void testFindUserByIdService() throws Exception {
		User user = createUser(testName);
		assertNotNull(user);
		assertNotNull(user.getId());
		
		User foundUser = userService.findById(user.getId());
		assertNotNull(foundUser);
		assertNotNull(foundUser.getId());
	}

}
