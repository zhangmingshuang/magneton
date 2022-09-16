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

import org.magneton.test.model.generate.util.LuhnUtils;
import org.magneton.test.util.StringUtil;

/**
 *
 *
 * <pre>
 * 银行卡号校验类
 * Created by Binary Wang on 2018/3/22.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class BankCardNumberValidator {

	/**
	 * 校验银行卡号是否合法
	 * @param cardNo 银行卡号
	 * @return 是否合法
	 */
	public static boolean validate(String cardNo) {
		if (!StringUtil.isNumeric(cardNo)) {
			return false;
		}

		if (cardNo.length() > 19 || cardNo.length() < 16) {
			return false;
		}

		int luhnSum = LuhnUtils.getLuhnSum(cardNo.substring(0, cardNo.length() - 1).trim().toCharArray());
		char checkCode = (luhnSum % 10 == 0) ? '0' : (char) ((10 - luhnSum % 10) + '0');
		return cardNo.substring(cardNo.length() - 1).charAt(0) == checkCode;
	}

}
