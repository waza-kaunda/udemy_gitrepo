package com.devopsbuddy.utils;

import javax.servlet.http.HttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;
import com.devopsbuddy.web.domain.fronted.BasicAccountPayload;

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

	public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {
		
		User user = new User();
		user.setUsername(frontendPayload.getUsername());
		user.setPassword(frontendPayload.getPassword());
		user.setFirstName(frontendPayload.getFirstName());
		user.setLastName(frontendPayload.getLastName());
		user.setEmail(frontendPayload.getEmail());
		user.setPhoneNumber(frontendPayload.getPhoneNumber());
		user.setCountry(frontendPayload.getCountry());
		user.setEnabled(true);
		user.setDescription(frontendPayload.getDescription());
		
		return user;
	}


}
