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

import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public abstract class AbstractBooleanSupplier<T> extends AbstractSupplier<T> implements BooleanSupplier {

	protected AbstractBooleanSupplier(@Nullable Object obj) {
		super(obj);
	}

	@Override
	public boolean getAsBoolean() {
		try {
			return this.doBooleanJudge();
		}
		finally {
			this.takeErrors();
		}
	}

	public BooleanSupplier sout() {
		this.setPrint(true);
		return this;
	}

	protected abstract boolean doBooleanJudge();

}
