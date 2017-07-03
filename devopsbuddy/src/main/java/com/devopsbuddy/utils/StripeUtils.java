package com.devopsbuddy.utils;

import java.util.HashMap;
import java.util.Map;

import com.devopsbuddy.web.domain.fronted.ProAccountPayload;

public class StripeUtils {

	public static String STRIPE_CARD_NUMBER_KEY = "number";
	public static String STRIPE_EXPIRY_MONTH_KEY = "exp_month";
	public static String STRIPE_EXPIRY_YEAR_KEY = "exp_year";
	public static String STRIPE_CVC_KEY = "cvc";
	public static String STRIPE_CARD_KEY = "card";

	private StripeUtils() {
		throw new AssertionError("Non instantiable");
	}

	/**
	 * Given the card info provided by the user in the front-end, it returns a parameters map
	 * @param payload
	 * @return
	 */
	public static Map<String, Object> extractTokenParamsFromSignupPayload(ProAccountPayload payload) {
		Map<String, Object> tokenParams = new HashMap<>();
		Map<String, Object> cardParams = new HashMap<>();
		
		cardParams.put(STRIPE_CARD_NUMBER_KEY, payload.getCardNumber());
		cardParams.put(STRIPE_EXPIRY_MONTH_KEY, Integer.valueOf( payload.getCardMonth()));
		cardParams.put(STRIPE_EXPIRY_YEAR_KEY, Integer.valueOf(payload.getCardYear()));
		cardParams.put(STRIPE_CVC_KEY, payload.getCardCode());
		
		tokenParams.put(STRIPE_CARD_KEY, cardParams);
		
		return tokenParams;
		
	}

}
