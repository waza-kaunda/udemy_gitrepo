package com.devopsbuddy.web.domain.fronted;

import java.io.Serializable;

public class FeedbackPojo implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String firstName;
	private String lastName;
	private String feedback;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeedbackPojo [email=");
		builder.append(email);
		builder.append(", firstname=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", feedback=");
		builder.append(feedback);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
