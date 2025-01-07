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

import com.aliyun.openservices.aliyun.log.producer.Callback;
import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import com.aliyun.openservices.log.common.LogItem;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * sls日志生产者
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class Log {

	private static List<Callback> CALLBACKS = null;

	public static void registerCallback(Callback callback) {
		if (CALLBACKS == null) {
			synchronized (Log.class) {
				if (CALLBACKS == null) {
					CALLBACKS = new CopyOnWriteArrayList<>();
				}
			}
		}
		CALLBACKS.add(callback);
	}

	private final LogProducer logProducer;

	private final SlsConfig.Config config;

	public Log(SlsConfig.Config config, ProducerConfig producerConfig, ProjectConfig projectConfig) {
		this.config = Objects.requireNonNull(config, "config is null");
		this.logProducer = new LogProducer(Objects.requireNonNull(producerConfig, "producerConfig is null"));
		this.logProducer.putProjectConfig(Objects.requireNonNull(projectConfig, "projectConfig is null"));
	}

	public void send(String topic, LogItem logItem) throws ProducerException, InterruptedException {
		Preconditions.checkNotNull(logItem, "logItem is null");

		topic = MoreObjects.firstNonNull(topic, "");
		if (Strings.isNullOrEmpty(topic)) {
			topic = MoreObjects.firstNonNull(this.config.getTopic(), "");
		}
		String source = MoreObjects.firstNonNull(this.config.getSource(), "");
		String shardHash = this.config.getShardHash();
		if (Strings.isNullOrEmpty(shardHash)) {
			shardHash = null;
		}
		this.logProducer.send(this.config.getProject(), this.config.getLogStore(), topic, source, shardHash, logItem,
				result -> {
					if (CALLBACKS != null) {
						CALLBACKS.forEach(callback -> callback.onCompletion(result));
					}
				});
	}

	/**
	 * 发送日志
	 * @param logItem 日志
	 * @throws ProducerException 生产者异常
	 * @throws InterruptedException 中断异常
	 */
	public void send(LogItem logItem) throws ProducerException, InterruptedException {
		this.send(null, logItem);
	}

	/**
	 * 发送日志
	 * <p>
	 * 如果发送失败，不会抛出异常
	 * @param logItem 日志
	 * @return 是否发送成功
	 */
	public boolean flySend(LogItem logItem) {
		try {
			this.send(logItem);
			return true;
		}
		catch (ProducerException | InterruptedException e) {
			log.error(String.format("send msg %s to sls error", logItem.ToJsonString()), e);
		}
		return false;
	}

	public void close() {
		try {
			this.logProducer.close();
		}
		catch (InterruptedException | ProducerException e) {
			// ignore
		}
	}

}
