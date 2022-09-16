/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.model.generate.bank;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.magneton.test.model.generate.base.AbstractGenericGenerator;
import org.magneton.test.model.generate.util.LuhnUtils;
import org.magneton.test.util.StringUtil;

/**
 *
 *
 * <pre>
 *  生成随机银行卡号：
 *
 *  参考：效验是否为银行卡，用于验证：
 *  现行 16 位银联卡现行卡号开头 6 位是 622126～622925 之间的，7 到 15 位是银行自定义的，
 *  可能是发卡分行，发卡网点，发卡序号，第 16 位是校验码。
 *  16 位卡号校验位采用 Luhm 校验方法计算：
 * 1，将未带校验位的 15 位卡号从右依次编号 1 到 15，位于奇数位号上的数字乘以 2
 * 2，将奇位乘积的个十位全部相加，再加上所有偶数位上的数字
 * 3，将加法和加上校验位能被 10 整除。
 * </pre>
 */
public class BankCardNumberGeneratorAbstract extends AbstractGenericGenerator {

	private static AbstractGenericGenerator instance = new BankCardNumberGeneratorAbstract();

	private BankCardNumberGeneratorAbstract() {
	}

	public static AbstractGenericGenerator getInstance() {
		return instance;
	}

	/**
	 * 根据给定前六位生成卡号
	 * @param prefix 前六位
	 * @return 银行卡号
	 */
	public static String generateByPrefix(Integer prefix) {
		Random random = ThreadLocalRandom.current();
		String bardNo = prefix + StringUtil.leftPad(random.nextInt(999999999) + "", 9, "0");

		char[] chs = bardNo.trim().toCharArray();
		int luhnSum = LuhnUtils.getLuhnSum(chs);
		char checkCode = luhnSum % 10 == 0 ? '0' : (char) (10 - luhnSum % 10 + '0');
		return bardNo + checkCode;
	}

	/**
	 * 根据银行名称 及银行卡类型生成对应卡号
	 * @param bankName 银行名称
	 * @param cardType 银行卡类型
	 * @return 银行卡号
	 */
	public static String generate(BankNameEnum bankName, BankCardTypeEnum cardType) {
		Integer[] candidatePrefixes = null;
		if (cardType == null) {
			candidatePrefixes = bankName.getAllCardPrefixes();
		}
		else {
			switch (cardType) {
			case DEBIT:
				candidatePrefixes = bankName.getDebitCardPrefixes();
				break;
			case CREDIT:
				candidatePrefixes = bankName.getCreditCardPrefixes();
				break;
			default:
			}
		}

		if (candidatePrefixes == null || candidatePrefixes.length == 0) {
			throw new RuntimeException("没有该银行的相关卡号信息");
		}

		Integer prefix = candidatePrefixes[ThreadLocalRandom.current().nextInt(candidatePrefixes.length)];
		return generateByPrefix(prefix);
	}

	@Override
	public String generate() {
		Random random = this.getRandomInstance();
		// ContiguousSet<Integer> sets = ContiguousSet
		// .create(Range.closed(622126, 622925), DiscreteDomain.integers());
		// ImmutableList<Integer> list = sets.asList();

		Integer prev = 622126 + random.nextInt(925 + 1 - 126);
		return generateByPrefix(prev);
	}

}
