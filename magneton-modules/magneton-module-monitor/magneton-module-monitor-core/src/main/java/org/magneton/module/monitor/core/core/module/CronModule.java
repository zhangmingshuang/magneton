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

package org.magneton.module.monitor.core.core.module;

import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 事件
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@ToString
public class CronModule extends BaseModule<CronModule> {

	/**
	 * cron表达式 Key
	 */
	public static final String CRON_KEY = "cron";

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 应该执行时间 Key
	 */
	public static final String SHOULD_TIME_KEY = "shouldTime";

	/**
	 * 应该执行时间，yyyy-MM-dd HH:mm:ss
	 */
	private String shouldTime;

	/**
	 * 丢失的间隔时间 Key
	 */
	public static final String TOTAL_INTERVAL_KEY = "totalInterval";

	/**
	 * 丢失的总间隔时间，毫秒
	 */
	private long totalInterval;

	/**
	 * 执行间隔 Key
	 */
	public static final String INTERVAL_KEY = "interval";

	/**
	 * 执行间隔
	 */
	private String interval;

	/**
	 * 间隔次数 Key
	 */
	public static final String INTERVAL_COUNT_KEY = "intervalCount";

	/**
	 * 间隔次数
	 */
	private int intervalCount;

	@Override
	public Map<String, String> getData() {
		Map<String, String> data = Maps.newHashMapWithExpectedSize(3);
		data.put(CRON_KEY, this.cron);
		data.put(SHOULD_TIME_KEY, this.shouldTime);
		data.put(INTERVAL_KEY, this.interval);
		data.put(INTERVAL_COUNT_KEY, String.valueOf(this.intervalCount));
		data.put(TOTAL_INTERVAL_KEY, String.valueOf(this.totalInterval));
		return data;
	}

}
