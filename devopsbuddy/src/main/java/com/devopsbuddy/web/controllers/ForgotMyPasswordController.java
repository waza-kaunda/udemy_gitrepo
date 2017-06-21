package com.devopsbuddy.web.controllers;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.I18NService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.backend.service.UserService;
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

	public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changepassword";

	private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";

	private static final String MESSAGE_ATTRIBUTE_NAME = "message";

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private I18NService i18NService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;
	
	@Value("${webmaster.email}")
	private String webMasterEmail;

	@GetMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordGet() {
		return EMAIL_ADDRESS_VIEW_NAME;
	}

	@PostMapping(value = FORGOT_PASSWORD_URL_MAPPING)
	public String forgotPasswordPost(HttpServletRequest request, String email, ModelMap model) {

		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

		if (passwordResetToken == null) {
			log.warn("Couldn't find a password reset token for email {}", email);
		} else {

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

	@GetMapping(value = CHANGE_PASSWORD_PATH)
	public String changeUserPasswordGet(@RequestParam("id") long id, @RequestParam("token") String token, Locale locale,
			ModelMap model) {
		
		if(StringUtils.isEmpty(token) || id == 0){
			log.error("Invalid user id {} or token value {}", id, token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
			return CHANGE_PASSWORD_VIEW_NAME;
		}
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);		
		if(passwordResetToken == null){
			log.warn("A token couldn't be found with value {}", token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
			return CHANGE_PASSWORD_VIEW_NAME;
		}
		
		User user = passwordResetToken.getUser();
		if(user.getId() != id){
			log.error("The user with id {} passed as parameter does not match the user id {} associated with the token {}", id, user.getId(), token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
			return CHANGE_PASSWORD_VIEW_NAME;
		}
		
		if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())){
			log.error("The token {} has expired", token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
			return CHANGE_PASSWORD_VIEW_NAME;

		}
		
		model.addAttribute("principalId", user.getId());
		
		// OK to proceed. We auto-authenticate the user so that in the POST request we can check if the user
		// is authenticated
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities() );
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return CHANGE_PASSWORD_VIEW_NAME;
	}
	
	@PostMapping(value = CHANGE_PASSWORD_PATH)
	public String changeUserPasswordPost(@RequestParam("principal_id") long userid, @RequestParam("password") String password, ModelMap model){
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null){
			log.error("An unauthenticated user tried to invoke the reset password POST method");
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request.");
			return CHANGE_PASSWORD_VIEW_NAME;
		}
		
		User user = (User) authentication.getPrincipal();
		if(user.getId() != userid){
			log.error("Security breach! User {} is trying to make a password reset request on behalf of {}", user.getId(), userid);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not autorized to perform this request.");
			return CHANGE_PASSWORD_VIEW_NAME;
		}
		
		userService.updateUserPassword(userid, password);
		log.info("Password successfully updated for user {}", user.getUsername());
		
		model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

		return CHANGE_PASSWORD_VIEW_NAME;
	}
}
