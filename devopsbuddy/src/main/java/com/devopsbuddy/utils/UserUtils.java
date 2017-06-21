package com.devopsbuddy.utils;

import javax.servlet.http.HttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;

public class UserUtils {
	
	private UserUtils(){
		throw new AssertionError("Non Instatiable");
	}
	
	public static User createUser(String username, String email) {

		User user = new User();
		user.setUsername(username);
		user.setPassword("secret");
		user.setEmail(email);
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setPhoneNumber("123456789");
		user.setCountry("SA");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blable.images.com/basicuser");

		return user;
	}

	public static String createPasswordResetUrl(HttpServletRequest request, long userid,
			String token) {
		
		String passwordResetUrl = 
				request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ":"
				+ request.getServerPort()
				+ request.getContextPath()
				+ ForgotMyPasswordController.CHANGE_PASSWORD_PATH
				+ "?id="
				+ userid
				+ "&token="
				+ token;
		
		return passwordResetUrl ;
	}


}
