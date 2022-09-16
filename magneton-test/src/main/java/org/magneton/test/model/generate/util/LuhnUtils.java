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

package org.magneton.test.model.generate.util;

/**
 *
 *
 * <pre>
 * Luhn算法工具类
 * Created by Binary Wang on 2018/3/22.
 * </pre>
 *
 * @author other
 */
public class LuhnUtils {

	/**
	 * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位 该校验的过程： 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
	 * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
	 * @param chs chs
	 * @return 校验位
	 */
	public static int getLuhnSum(char[] chs) {
		int luhnSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhnSum += k;
		}
		return luhnSum;
	}

}
