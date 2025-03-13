/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.monitor.core.module;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.monitor.core.Biz;

import java.util.Map;

/**
 * 时间消耗模型
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class TimeUsedModule extends BaseModule<TimeUsedModule> {

	/**
	 * 时间消耗 Key
	 */
	public static final String TIME_USED_KEY = "timeUsed";

	/**
	 * 耗时，毫秒。
	 */
	private long timeUsed;

	public TimeUsedModule(Biz biz) {
		super(biz);
	}

	@Override
	public Map<String, String> getData() {
		Map<String, String> data = Maps.newHashMapWithExpectedSize(1);
		data.put(TIME_USED_KEY, String.valueOf(this.timeUsed));
		return data;
	}

}