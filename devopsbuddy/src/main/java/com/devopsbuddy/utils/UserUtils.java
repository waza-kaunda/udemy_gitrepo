package com.devopsbuddy.utils;

import com.devopsbuddy.backend.persistence.domain.backend.User;

public class UserUtils {
	
	private UserUtils(){
		throw new AssertionError("Non Instatiable");
	}
	
	public static User createBasicUser() {

		User user = new User();
		user.setUsername("basicUser");
		user.setPassword("secret");
		user.setEmail("me@emaple.com");
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setPhoneNumber("123456789");
		user.setCountry("SA");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blable.images.com/basicuser");

		return user;
	}


}
