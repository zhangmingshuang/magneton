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

package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;

/**
 * 验证注解的元素值大于等于 {@link Min} 指定的 value 值.
 *
 * <p>
 * 验证注解的元素值大于等于 {@link DecimalMin} 指定的 value 值
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 */
@TestComponent
public class MinConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
		long min = (long) metadata.get("value");

		this.setByte(config, min);
		this.setShort(config, min);
		this.setInt(config, min);
		this.setLong(config, min);
		this.setBigDecimal(config, min);
		this.setBigInteger(config, min);
	}

	public void setBigInteger(Config config, long min) {
		BigInteger minBigInterger = config.getMinBigInteger();
		if (minBigInterger == null || minBigInterger.longValue() < min) {
			config.setMinBigInteger(BigInteger.valueOf(min));
		}
		BigInteger maxBigInteger = config.getMaxBigInteger();
		if (maxBigInteger == null || maxBigInteger.longValue() < min) {
			config.setMaxBigInteger(BigInteger.valueOf(min));
		}
	}

	public void setBigDecimal(Config config, long min) {
		BigDecimal minBigDecimal = config.getMinBigDecimal();
		if (minBigDecimal == null || minBigDecimal.compareTo(BigDecimal.valueOf(min)) < 0) {
			// 如果当前设置的最小值比预期的最小值小，则使用该预期的最小值
			config.setMinBigDecimal(BigDecimal.valueOf(min));
		}
		BigDecimal maxBigDecimal = config.getMaxBigDecimal();
		if (maxBigDecimal == null || maxBigDecimal.compareTo(BigDecimal.valueOf(min)) < 0) {
			// 如果当前设置的最大值比预期的最小值小，则使用该预期的最小值为最大值
			config.setMaxBigDecimal(BigDecimal.valueOf(min));
		}
	}

	public void setLong(Config config, long min) {
		Long minLong = config.getMinLong();
		if (minLong == null || minLong < min) {
			config.setMinLong(min);
		}
		Long maxLong = config.getMaxLong();
		if (maxLong == null || maxLong < min) {
			config.setMaxLong(min);
		}
	}

	public void setInt(Config config, long min) {
		Integer minInt = config.getMinInt();
		if (minInt == null || minInt < min) {
			config.setMinInt((int) min);
		}
		Integer maxInt = config.getMaxInt();
		if (maxInt == null || maxInt < min) {
			config.setMaxInt((int) min);
		}
	}

	public void setShort(Config config, long min) {
		Short minShort = config.getMinShort();
		if (minShort == null || minShort.intValue() < min) {
			config.setMinShort((short) min);
		}
		Short maxShort = config.getMaxShort();
		if (maxShort == null || maxShort.intValue() < min) {
			config.setMaxShort((short) min);
		}
	}

	public void setByte(Config config, long min) {
		Byte minByte = config.getMinByte();
		if (minByte == null || minByte.intValue() < min) {
			config.setMinByte((byte) min);
		}
		Byte maxByte = config.getMaxByte();
		if (maxByte == null || maxByte.intValue() < min) {
			config.setMaxByte((byte) min);
		}
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Min.class, DecimalMin.class };
	}

}
