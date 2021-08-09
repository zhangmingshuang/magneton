package cn.nascent.framework.test.generate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/9
 * @since 2.0.0
 */
class TestValueGeneratorTest {

	@Test
	void chineseAddress() {
		String value = TestValueGenerator.getInstance().chineseAddress();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseIdCardNumber() {
		String value = TestValueGenerator.getInstance().chineseIdCardNumber();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseIdCardIssueOrg() {
		String value = TestValueGenerator.getInstance().chineseIdCardIssueOrg();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseIdCardValidPeriod() {
		String value = TestValueGenerator.getInstance().chineseIdCardValidPeriod();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseMobileNumber() {
		String value = TestValueGenerator.getInstance().chineseMobileNumber();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseFakeMobileNumber() {
		String value = TestValueGenerator.getInstance().chineseFakeMobileNumber();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseName() {
		String value = TestValueGenerator.getInstance().chineseName();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void chineseOddName() {
		String value = TestValueGenerator.getInstance().chineseOddName();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void englishName() {
		String value = TestValueGenerator.getInstance().englishName();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

	@Test
	void email() {
		String value = TestValueGenerator.getInstance().email();
		System.out.println(value);
		Assertions.assertNotNull(value);
	}

}