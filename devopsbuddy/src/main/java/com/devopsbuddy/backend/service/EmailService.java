/**
 * 
 */
package com.devopsbuddy.backend.service;

import org.springframework.mail.SimpleMailMessage;

import com.devopsbuddy.web.domain.fronted.FeedbackPojo;

/**
 * @author WazaK
 *
 */
public interface EmailService {
	
	/**
	 * Sends an email with the content in the Feedback Pojo
	 * @param feedbackPojo
	 */
	public void sendFeedbackEmail(FeedbackPojo feedbackPojo);
	
	/**
	 * Sends a generic email message 
	 * @param message
	 */
	public void sendGenericEmail(SimpleMailMessage message);

}
