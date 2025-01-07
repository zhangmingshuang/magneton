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

import cn.nascent.tech.gaia.biz.monitor.core.Biz;
import com.google.common.collect.Maps;
import lombok.ToString;

import java.util.Map;

/**
 * 业务指标
 * <p>
 * 用于记录业务指标、指标可能包含对一个指标记录次数、记录平均值、记录总和，
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@ToString
public class MetricModule extends BaseModule<MetricModule> {

	/**
	 * 连动指标 key = 指标类开导，value = 指标值
	 */
	private final Map<Kind, Long> metrics = Maps.newConcurrentMap();

	public static MetricModule valueOf(String bizId) {
		return new MetricModule(Biz.of(bizId));
	}

	/**
	 * 记录次数
	 * @param count 次数
	 * @return {@link MetricModule}
	 */
	public MetricModule count(long count) {
		this.metrics.compute(Kind.COUNT, (k, v) -> {
			if (v == null) {
				return count;
			}
			return v + count;
		});
		return this;
	}

	/**
	 * 记录平均时间
	 * @param avg 平均时间
	 * @return {@link MetricModule}
	 */
	public MetricModule avg(long avg) {
		this.metrics.compute(Kind.AVG, (k, v) -> {
			if (v == null) {
				return avg;
			}
			return v + avg;
		});
		return this;
	}

	/**
	 * 记录总数
	 * @param sum 总数
	 * @return {@link MetricModule}
	 */
	public MetricModule sum(long sum) {
		this.metrics.compute(Kind.SUM, (k, v) -> {
			if (v == null) {
				return sum;
			}
			return v + sum;
		});
		return this;
	}

	public MetricModule(Biz biz) {
		super(biz);
	}

	@Override
	public Map<String, String> getData() {
		Map<String, String> data = Maps.newHashMapWithExpectedSize(this.metrics.size());
		this.metrics.forEach((k, v) -> data.put(k.name(), String.valueOf(v)));
		return data;
	}

	public enum Kind {

		/**
		 * 记录次数
		 */
		COUNT,
		/**
		 * 记录平均值
		 */
		AVG,
		/**
		 * 记录总和
		 */
		SUM

	}

}
