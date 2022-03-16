package org.magneton.support.api.auth.properties;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
class ApiAuthPropertyTest {

	ApiAuthProperty apiAuthProperty = new ApiAuthProperty();

	@Test
	void test() {
		ApiAuthProperty.Sms sms = new ApiAuthProperty.Sms();
		Pattern mobileRegex = sms.getMobileRegex();
		Assertions.assertTrue(mobileRegex.matcher("13860132592").matches());

		Assertions.assertFalse(mobileRegex.matcher("23888888888").matches());
	}

}