package org.magneton.test.test.model.generate;

/**
 * 随机数据生成.
 *
 * @author zhangmsh 2021/8/9
 * @since 2.0.0
 */
public class ValueGenerator {

	private static final ValueGenerator VALUE_GENERATOR = new ValueGenerator();

	private ValueGenerator() {
	}

	public static ValueGenerator getInstance() {
		return VALUE_GENERATOR;
	}

	/**
	 * 生成住址
	 * @return 如：云南省德宏傣族景颇族自治州织魏刺路77号市劈减小区13单元244室
	 */
	public String chineseAddress() {
		return ChineseAddressGeneratorAbstract.getInstance().generate();
	}

	/**
	 * 生成身份证号码
	 * @return 如：116789198610130916
	 */
	public String chineseIdCardNumber() {
		return ChineseIDCardNumberGeneratorAbstract.getInstance().generate();
	}

	/**
	 * 生成身份证签发机构
	 * @return 如：榆林市公安局某某分局
	 */
	public String chineseIdCardIssueOrg() {
		return ChineseIDCardNumberGeneratorAbstract.generateIssueOrg();
	}

	/**
	 * 生成身份证有效期
	 * @return 如：19960208-20160208
	 */
	public String chineseIdCardValidPeriod() {
		return ChineseIDCardNumberGeneratorAbstract.generateValidPeriod();
	}

	/**
	 * 生成手机号
	 * @return 如：15357370748
	 */
	public String chineseMobileNumber() {
		return ChineseMobileNumberGeneratorAbstract.getInstance().generate();
	}

	/**
	 * 生成假手机号
	 * @return 如：19485480310
	 */
	public String chineseFakeMobileNumber() {
		return ChineseMobileNumberGeneratorAbstract.getInstance().generateFake();
	}

	/**
	 * 生成常见姓名
	 * @return 如：方溶
	 */
	public String chineseName() {
		return ChineseNameGeneratorAbstract.getInstance().generate();
	}

	/**
	 * 生成带有偏僻字的姓名
	 * @return 如：范噵
	 */
	public String chineseOddName() {
		return ChineseNameGeneratorAbstract.getInstance().generateOdd();
	}

	/**
	 * 生成英文姓名
	 * @return 如：Rowan
	 */
	public String englishName() {
		return EnglishNameGeneratorAbstract.getInstance().generate();
	}

	/**
	 * 生成邮箱
	 * @return 如：vr8tjzdayn@dxldw.zfz
	 */
	public String email() {
		return EmailAddressGeneratorAbstract.getInstance().generate();
	}

}
