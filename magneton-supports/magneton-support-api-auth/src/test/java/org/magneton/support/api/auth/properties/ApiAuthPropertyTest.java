package org.magneton.support.api.auth.properties;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.sms.property.SmsProperty;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
class ApiAuthPropertyTest {

	@Test
	void test() {
		SmsProperty smsProperty = new SmsProperty();
		Pattern mobileRegex = smsProperty.getMobileRegex();
		Assertions.assertTrue(mobileRegex.matcher("13860132592").matches());

		Assertions.assertFalse(mobileRegex.matcher("23888888888").matches());
	}

}