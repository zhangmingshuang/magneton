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

package org.magneton.test.config;

import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.PrimitiveUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@ConfigWith(InjectType.DEFAULT_VALUE)
public class DefaultValueConfigProcessor implements ConfigProcessor {

	@Override
	public int nextSize(Config config, Definition definition) {
		return 0;
	}

	@Override
	public boolean nullable(Config config, Definition definition) {
		return false;
	}

	@Override
	public BigDecimal nextBigDecimal(Config config, Definition definition) {
		return null;
	}

	@Override
	public BigInteger nextBigInteger(Config config, Definition definition) {
		return null;
	}

	@Override
	public Byte nextByte(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (byte) 0 : null;
	}

	@Override
	public Short nextShort(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (short) 0 : null;
	}

	@Override
	public Integer nextInt(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0 : null;
	}

	@Override
	public Long nextLong(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0L : null;
	}

	@Override
	public Float nextFloat(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0.0F : null;
	}

	@Override
	public Double nextDouble(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0.0D : null;
	}

	@Override
	public Boolean nextBoolean(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? false : null;
	}

	@Override
	public Character nextCharacter(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (char) 0 : null;
	}

	@Override
	public String nextString(Config config, Definition definition) {
		return null;
	}

	@Override
	public Date nextDate(Config config, Definition definition) {
		return null;
	}

}
