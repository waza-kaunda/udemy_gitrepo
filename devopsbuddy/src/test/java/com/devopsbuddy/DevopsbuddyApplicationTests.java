package com.devopsbuddy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.web.i18n.I18NService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsbuddyApplicationTests {

	@Autowired
	private I18NService i18NService;
	
	@Test
	public void testMessageByLocaleService() throws Exception {
		
		String expectedResult = "Dev Ops Buddy Kickstart";
		String messageId = "index.main.callout";
		String actual = i18NService.getMessages(messageId);
		
		assertEquals("The actual and expected Strings don't match", expectedResult, actual);
		
	}

}
