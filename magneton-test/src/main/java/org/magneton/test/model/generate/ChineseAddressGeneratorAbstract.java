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
import org.magneton.test.model.generate.util.ChineseCharUtils;

public class ChineseAddressGeneratorAbstract extends AbstractGenericGenerator {

	private static AbstractGenericGenerator instance = new ChineseAddressGeneratorAbstract();

	private ChineseAddressGeneratorAbstract() {
	}

	public static AbstractGenericGenerator getInstance() {
		return instance;
	}

	private static String genProvinceAndCity() {
		return ChineseAreaList.provinceCityList
				.get(ThreadLocalRandom.current().nextInt(0, ChineseAreaList.provinceCityList.size()));
	}

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder(genProvinceAndCity());
		result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "路");
		result.append(ThreadLocalRandom.current().nextInt(1, 8000) + "号");
		result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "小区");
		result.append(ThreadLocalRandom.current().nextInt(1, 20) + "单元");
		result.append(ThreadLocalRandom.current().nextInt(101, 2500) + "室");
		return result.toString();
	}

}
