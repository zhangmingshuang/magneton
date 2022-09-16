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

package org.magneton.test.supplier;

import java.util.Date;

/**
 * 判断两个相同类型的对象的数据是否全部相等.
 *
 * <p>
 * 如果两个对象的任意一个数据不相等，则返回 {@code false}
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public class ValueEqualsBooleanSupplier
		extends AbstractEqualsSupplier<ValueEqualsBooleanSupplier> {

	/** Date类型的时间比较时允许的时间差异，单位毫秒 */
	private int dateTimeDiffTolerate = 999;

	public <E> ValueEqualsBooleanSupplier(E element1, E element2) {
		super(element1, element2);
	}

	@Override
	protected boolean doFeieldValueNotEqualsProcess(Class<?> fieldClass, Object fieldValue1, Object fieldValue2) {
		if (Date.class.isAssignableFrom(fieldClass) || java.sql.Date.class.isAssignableFrom(fieldClass)) {
			long time1 = ((Date) fieldValue1).getTime();
			long time2 = ((Date) fieldValue2).getTime();
			return Math.abs(time1 - time2) < this.dateTimeDiffTolerate;
		}
		return false;
	}

	public void setDateTimeDiffTolerate(int dateTimeDiffTolerate) {
		this.dateTimeDiffTolerate = dateTimeDiffTolerate;
	}

	public int getDateTimeDiffTolerate() {
		return this.dateTimeDiffTolerate;
	}

}
