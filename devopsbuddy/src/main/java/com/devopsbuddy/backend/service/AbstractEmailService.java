package com.devopsbuddy.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.devopsbuddy.web.domain.fronted.FeedbackPojo;

public abstract class AbstractEmailService implements EmailService {


	@Value("${default.to.address}")
	private String defaultToAddress;
	
	/**
	 * Creates a simple mail message from a feedback pojo
	 * @param feedback
	 * @return
	 */
	protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(FeedbackPojo feedback){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(defaultToAddress);
		message.setFrom(feedback.getEmail());
		message.setSubject("[DevOps Buddy]: Feedback recieved from " + feedback.getFirstName() + " " + feedback.getLastName() + "!");
		message.setText(feedback.getFeedback());
		return message;
	}
	
	@Override
	public void sendFeedbackEmail(FeedbackPojo feedbackPojo) {
		sendGenericEmail(prepareSimpleMailMessageFromFeedbackPojo(feedbackPojo));
	}
	
}
