package cn.nascent.framework.test.generate;

import cn.nascent.framework.test.generate.base.GenericGenerator;
import cn.nascent.framework.test.util.RandomStringUtils;

public class EmailAddressGenerator extends GenericGenerator {

	private static GenericGenerator instance = new EmailAddressGenerator();

	private EmailAddressGenerator() {
	}

	public static GenericGenerator getInstance() {
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
