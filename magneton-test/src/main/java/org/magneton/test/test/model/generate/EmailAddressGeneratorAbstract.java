package org.magneton.test.test.model.generate;

import org.magneton.test.test.model.generate.base.AbstractGenericGenerator;
import org.apache.commons.lang3.RandomStringUtils;

public class EmailAddressGeneratorAbstract extends AbstractGenericGenerator {

	private static AbstractGenericGenerator instance = new EmailAddressGeneratorAbstract();

	private EmailAddressGeneratorAbstract() {
	}

	public static AbstractGenericGenerator getInstance() {
		return instance;
	}

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		result.append(RandomStringUtils.randomAlphanumeric(10));
		result.append("@");
		result.append(RandomStringUtils.randomAlphanumeric(5));
		result.append(".");
		result.append(RandomStringUtils.randomAlphanumeric(3));

		return result.toString().toLowerCase();
	}

}
