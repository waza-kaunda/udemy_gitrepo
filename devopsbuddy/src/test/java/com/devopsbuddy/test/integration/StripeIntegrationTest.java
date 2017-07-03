package com.devopsbuddy.test.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.backend.service.StripeService;
import com.devopsbuddy.enums.PlansEnum;
import com.stripe.Stripe;
import com.stripe.model.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StripeIntegrationTest {

	public static final String TEST_CC_CVC_NBR = "851";

	public static final int TEST_CC_EXP_MONTH = 6;

	public static final String TEST_CC_NUMBER = "4242424242424242";

	@Autowired
	private StripeService stripeService;
	
	@Autowired
	private String stripeKey;
	
	@Before
	public void init(){
		assertNotNull(stripeKey);
		Stripe.apiKey = stripeKey;
	}
	
	@Test
	public void createStripeCustomer() throws Exception{
		
		Map<String, Object> tokenParams = new HashMap<>();
		Map<String, Object> cardParams = new HashMap<>();
		
		cardParams.put("number", TEST_CC_NUMBER);
		cardParams.put("exp_month", TEST_CC_EXP_MONTH);
		cardParams.put("exp_year", LocalDate.now(Clock.systemUTC()).getYear() + 3);
		cardParams.put("cvc", TEST_CC_CVC_NBR);
		
		tokenParams.put("card", cardParams);
		
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("description", "Customer for test@example.com");
		customerParams.put("plan", PlansEnum.PRO.getId());
		
		String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
		assertThat(stripeCustomerId, is(notNullValue()));
		
		Customer cu = Customer.retrieve(stripeCustomerId);
		cu.delete();
	}
}
