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

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

import java.lang.annotation.Annotation;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import javax.annotation.Nullable;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

/**
 * {@code @PastOrPresent} 验证注解的元素值（日期类型）比当前时间早，或者是当前时间
 *
 * <p>
 * {@code @Past} 验证注解的元素值（日期类型）比当前时间早
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class PastConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Long minDateMillis = config.getMinDateMillis();
		Long maxDateMillis = config.getMaxDateMillis();

		Class clazz = definition.getClazz();
		if (YearMonth.class.isAssignableFrom(clazz)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			minDateMillis = Math.min(minDateMillis, calendar.getTimeInMillis());
		}
		else if (LocalTime.class.isAssignableFrom(clazz)) {
			// 时、分、秒
			// 任何进位都可能得到一个未来的时间
			minDateMillis = Math.min(minDateMillis, System.currentTimeMillis() - 10 * 1000L);
			maxDateMillis = minDateMillis;
		}
		else if (Year.class.isAssignableFrom(clazz)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -1);
			minDateMillis = Math.min(minDateMillis, calendar.getTimeInMillis());
		}
		else {
			if (minDateMillis == null || minDateMillis < System.currentTimeMillis()) {
				minDateMillis = System.currentTimeMillis() - 86400 * 1000L;
			}
		}

		if (maxDateMillis > minDateMillis) {
			maxDateMillis = minDateMillis - 86400 * 1000L;
		}
		config.setMinDateMillis(minDateMillis).setMaxDateMillis(maxDateMillis);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Past.class, PastOrPresent.class };
	}

}
