package org.magneton.test.model.generate.bank;

/**
 * 银行卡类型枚举类
 * @author other
 */
public enum BankCardTypeEnum {

	/** 借记卡/储蓄卡 */
	DEBIT("借记卡/储蓄卡"),
	/** 信用卡/贷记卡 */
	CREDIT("信用卡/贷记卡");

	private final String name;

	BankCardTypeEnum(String name) {
		this.name = name;
	}

}
