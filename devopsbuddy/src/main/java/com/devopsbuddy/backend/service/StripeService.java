package com.devopsbuddy.backend.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Token;

@Service
public class StripeService {

	
	private static final Logger log = LoggerFactory.getLogger(StripeService.class);

	@Autowired
	private String stripeKey;
	
	public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams){
		
		Stripe.apiKey = stripeKey;
		
		String stripeCustomerId = null;
		
		try{
			Token token = Token.create(tokenParams);
			customerParams.put("source", token.getId());
			
			Customer customer = Customer.create(customerParams);
			stripeCustomerId = customer.getId();
			
		}catch (AuthenticationException e) {
			log.error("An authentication exception occured while creating the stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e); 
		}catch(InvalidRequestException e){
			log.error("An invalid request exception occured while creating the stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e);
		}catch(APIConnectionException e){
			log.error("An API connection exception occured while creating the stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e);
		}catch(CardException e){
			log.error("A credit card exception occured while creating the stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e);
		}catch(APIException e){
			log.error("An API exception occured while creating the Stripe customer", e);
			throw new com.devopsbuddy.exceptions.StripeException(e);
		}
		
		return stripeCustomerId;
	}
}
