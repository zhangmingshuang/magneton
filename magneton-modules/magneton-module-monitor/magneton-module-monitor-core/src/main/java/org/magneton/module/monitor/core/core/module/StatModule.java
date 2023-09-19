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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 统计模型
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class StatModule extends BaseModule<StatModule> {

	/**
	 * 服务处理名称
	 */
	private String serviceProcessor;

	/**
	 * 服务处理名称 Key
	 */
	public static final String SERVICE_PROCESSOR_KEY = "serviceProcessor";

	/**
	 * 生产数量
	 */
	private long productNum;

	/**
	 * 生产数量 Key
	 */
	public static final String PRODUCT_NUM_KEY = "productNum";

	/**
	 * 消费数量
	 */
	private long consumeNum;

	/**
	 * 消费数量 Key
	 */
	public static final String CONSUME_NUM_KEY = "consumeNum";

	/**
	 * 消耗时间，毫秒
	 */
	private long timeUsed;

	/**
	 * 消耗时间 Key
	 */
	public static final String TIME_USED_KEY = "timeUsed";

	/**
	 * 延迟时间，毫秒
	 */
	private long timeDelayed;

	/**
	 * 延迟时间 Key
	 */
	public static final String TIME_DELAYED_KEY = "timeDelayed";

	@Override
	public Map<String, String> getData() {
		Map<String, String> data = Maps.newHashMapWithExpectedSize(5);
		data.put(SERVICE_PROCESSOR_KEY, this.serviceProcessor);
		data.put(PRODUCT_NUM_KEY, String.valueOf(this.productNum));
		data.put(CONSUME_NUM_KEY, String.valueOf(this.consumeNum));
		data.put(TIME_USED_KEY, String.valueOf(this.timeUsed));
		data.put(TIME_DELAYED_KEY, String.valueOf(this.timeDelayed));
		return data;
	}

}
