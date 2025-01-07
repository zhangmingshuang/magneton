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

package org.magneton.log;

import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * sls客户端
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class SlsClient {

	/**
	 * 发送日志端
	 */
	private static final Map<String, Log> LOGS = Maps.newHashMapWithExpectedSize(16);

	static {
		Runtime.getRuntime().removeShutdownHook(new Thread(() -> LOGS.values().forEach(Log::close)));
	}

	/**
	 * 日志配置
	 */
	@Nullable
	private final SlsConfig slsConfig;

	/**
	 * 默认日志配置
	 */
	private final SlsConfig defaultSlsConfig;

	public SlsClient(@Nullable SlsConfig slsConfig, SlsConfig defaultSlsConfig) {
		this.slsConfig = slsConfig;
		this.defaultSlsConfig = Objects.requireNonNull(defaultSlsConfig, "defaultSlsConfig must not be null");
	}

	public Log getLog() {
		return this.getLog("*");
	}

	/**
	 * 获取日志发送器
	 * @param id ID
	 * @return 返回对应的日志发送器，如果对应的日志发送器不存在，则返回默认的日志发送器
	 */
	public Log getLogOrDefault(String id) {
		Objects.requireNonNull(id, "id must not be null");
		return this.getLog(id);
	}

	private synchronized Log getLog(String id) {
		Log log = LOGS.get(id);
		if (log != null) {
			return log;
		}

		SlsConfig effective = this.slsConfig;
		SlsConfig.Config effectiveCfg = null;
		if (this.slsConfig != null) {
			// 如果是默认的配置，则所有的配置均采用被覆写的配置，不走默认配置选项
			Map<String, SlsConfig.Config> bizProducers = this.slsConfig.getBizProducer();
			if (bizProducers != null) {
				effectiveCfg = bizProducers.get(id);
			}
			if (effectiveCfg == null) {
				effectiveCfg = this.slsConfig.getProducer();
			}
		}
		if (effectiveCfg == null) {
			effective = this.defaultSlsConfig;
			Map<String, SlsConfig.Config> bizProducers = this.defaultSlsConfig.getBizProducer();
			if (bizProducers != null) {
				effectiveCfg = bizProducers.get(id);
			}
			if (effectiveCfg == null) {
				effectiveCfg = this.defaultSlsConfig.getProducer();
			}
		}

		Verify.verifyNotNull(effectiveCfg, "effectiveCfg(id:%s) must not be null", id);

		String endpoint = effective.getEndpoint();
		String accessKey = effective.getAccessKey();
		String accessSecret = effective.getAccessSecret();

		ProducerConfig producerConfig = new ProducerConfig();
		log = new Log(effectiveCfg, producerConfig,
				new ProjectConfig(effectiveCfg.getProject(), endpoint, accessKey, accessSecret));

		LOGS.put(id, log);
		return log;
	}

}
