package com.devopsbuddy.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;
import com.devopsbuddy.web.domain.fronted.BasicAccountPayload;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class UserUtilsUnitTest {

	private MockHttpServletRequest mockHttpServletRequest;
	
	private PodamFactory podamFactory;

	@Before
	public void init() throws Exception {
		mockHttpServletRequest = new MockHttpServletRequest();
		podamFactory = new PodamFactoryImpl();
	}

	@Test
	public void testPasswordResetEmailUrlConstruction() throws Exception {

		mockHttpServletRequest.setServerPort(8080);

		String token = UUID.randomUUID().toString();
		long userId = 123456;

		String expectedUrl = "http://localhost:8080" + ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId
				+ "&token=" + token;

		String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);

		assertEquals(expectedUrl, actualUrl);
	}

	@Test
	public void mapWebUserToDomainUser(){
		
		BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
		webUser.setEmail("me@example.com");
		
		User user = UserUtils.fromWebUserToDomainUser(webUser);
		assertNotNull(user);
		
		assertEquals("Username not matching",webUser.getUsername(), user.getUsername());
		assertEquals("password not matching",webUser.getPassword(), user.getPassword());
		assertEquals("fist name not matching",webUser.getFirstName(), user.getFirstName());
		assertEquals("last name not matching",webUser.getLastName(), user.getLastName());
		assertEquals("email not matching",webUser.getEmail(), user.getEmail());
		assertEquals("phonenumber not matching",webUser.getPhoneNumber(), user.getPhoneNumber());
		assertEquals("country not matching",webUser.getCountry(), user.getCountry());
		assertEquals("description not matching",webUser.getDescription(), user.getDescription());
		
		
	}
}
