package com.devopsbuddy.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.I18NService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.utils.UserUtils;

@Controller
public class ForgotMyPasswordController {

	/**
	 * the application logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ForgotMyPasswordController.class);
	
	public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
	
	public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

	private static final String MAIL_SET_KEY = "mailSent";

	public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
	
	public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text"; 
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	@Autowired
	private I18NService i18NService;
	
	@Autowired
	private EmailService emailService;	
	
	@Value("${webmaster.email}")
	private String webMasterEmail;
	
	
	@GetMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordGet(){
		return EMAIL_ADDRESS_VIEW_NAME;
	}
	
	@PostMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordPost(HttpServletRequest request, String email, ModelMap model){
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);
		
		if(passwordResetToken == null){
			log.warn("Couldn't find a password reset token for email {}", email);			
		}else{
			
			User user = passwordResetToken.getUser();
			String token = passwordResetToken.getToken();			
			String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
			
			log.debug("Reset Password Url {}", resetPasswordUrl);
			
			String emailText = i18NService.getMessages(EMAIL_MESSAGE_TEXT_PROPERTY_NAME);
		
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("[Devopsbuddy]: Password Reset Request");
			mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
			mailMessage.setFrom(webMasterEmail);
			
			emailService.sendGenericEmail(mailMessage);
		}
		
		model.addAttribute(MAIL_SET_KEY, "true");
		
		return EMAIL_ADDRESS_VIEW_NAME;
	}

}
