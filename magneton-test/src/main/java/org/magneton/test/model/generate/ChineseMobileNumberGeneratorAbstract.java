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

package org.magneton.test.model.generate;

import java.util.concurrent.ThreadLocalRandom;

import org.magneton.test.model.generate.base.AbstractGenericGenerator;
import org.magneton.test.util.StringUtil;

public class ChineseMobileNumberGeneratorAbstract extends AbstractGenericGenerator {

	private static final int[] MOBILE_PREFIX = { 133, 153, 177, 180, 181, 189, 134, 135, 136, 137, 138, 139, 150, 151,
			152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186, 145, 147, 170 };

	private static ChineseMobileNumberGeneratorAbstract instance = new ChineseMobileNumberGeneratorAbstract();

	private ChineseMobileNumberGeneratorAbstract() {
	}

	public static ChineseMobileNumberGeneratorAbstract getInstance() {
		return instance;
	}

	private static String genMobilePre() {
		return "" + MOBILE_PREFIX[ThreadLocalRandom.current().nextInt(0, MOBILE_PREFIX.length)];
	}

	@Override
	public String generate() {
		return genMobilePre() + StringUtil.leftPad("" + ThreadLocalRandom.current().nextInt(0, 99999999 + 1), 8, "0");
	}

	/**
	 * 生成假的手机号，以19开头
	 * @return 假的手机号
	 */
	public String generateFake() {
		return "19" + StringUtil.leftPad("" + ThreadLocalRandom.current().nextInt(0, 999999999 + 1), 9, "0");
	}

}
