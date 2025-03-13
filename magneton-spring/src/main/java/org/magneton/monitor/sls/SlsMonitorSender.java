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

package org.magneton.monitor.sls;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import com.aliyun.openservices.log.common.LogItem;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.magneton.log.Log;
import org.magneton.log.SlsClient;
import org.magneton.log.SlsConfig;
import org.magneton.monitor.SlsMonitorAutoConfiguration;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.GMC;
import org.magneton.monitor.core.MonitorSender;
import org.magneton.monitor.core.MonitorSenders;
import org.magneton.monitor.core.module.Module;
import org.magneton.monitor.core.module.ModuleType;
import org.magneton.monitor.core.monitor.MonitorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

/**
 * sls发送器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
@Component
public class SlsMonitorSender implements MonitorSender {

	/**
	 * 时间格式化
	 */
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private SlsMonitorProperty property;

	private SlsClient slsClient;

	public SlsMonitorSender(@Autowired SlsMonitorProperty property) {
		this.property = Preconditions.checkNotNull(property, "property");
		this.init();
	}

	protected void init() {
		if (!SlsMonitorAutoConfiguration.isEnable()) {
			return;
		}
		this.slsClient = new SlsClient(
				(SlsConfig) MoreObjects.firstNonNull(this.property.getSls(), Collections.emptyMap())
					.get(SlsMonitorAutoConfiguration.getProfile()),
				InnerSlsConfig.getInstance().getSlsConfig(SlsMonitorAutoConfiguration.getProfile()));
		MonitorSenders.getInstance().register(this);
	}

	@PreDestroy
	public void destroy() {
		MonitorSenders.getInstance().unregister(this);
	}

	@Override
	public void send(Module module) {
		if (!SlsMonitorAutoConfiguration.isEnable()) {
			return;
		}

		Preconditions.checkNotNull(module, "module is null");

		Biz biz = module.getBiz();
		String bizId = biz.getId();
		String useCase = biz.getUseCase();
		String scenario = biz.getScenario();
		String message = module.getMessage();
		ModuleType moduleType = module.getModuleType();
		Map<String, String> data = module.getData();

		Map<String, String> context = Maps.newHashMap();
		if (module.isUseExt()) {
			MonitorContext monitorContext = module.getMonitorContext();
			if (monitorContext != null && !monitorContext.isEmpty()) {
				context.putAll(monitorContext.getContext());
			}
		}

		if (!CollectionUtil.isEmpty(data)) {
			context.putAll(data);
		}

		Map<String, String> globalContext = GMC.getGlobalContext();
		globalContext.forEach(context::putIfAbsent);

		context.put("bizId", bizId);

		if (!Strings.isNullOrEmpty(useCase)) {
			context.put("useCase", useCase);
		}
		else {
			context.putIfAbsent("useCase", "default");
		}
		if (!Strings.isNullOrEmpty(scenario)) {
			context.put("scenario", scenario);
		}
		else {
			context.putIfAbsent("scenario", "default");
		}

		context.put("monitor", moduleType.getType());
		context.put("message", MoreObjects.firstNonNull(message, "-"));
		if (!context.containsKey("timestamp")) {
			context.put("timestamp", FORMATTER.format(LocalDateTime.now()));
		}
		context.put("_bizId_", Biz.valueOfBizId(bizId, context.get("useCase"), context.get("scenario")));

		LogItem logItem = new LogItem();
		context.forEach(logItem::PushBack);

		Log slsLog = this.slsClient.getLogOrDefault(moduleType.getType());
		try {
			slsLog.send(logItem);
		}
		catch (ProducerException | InterruptedException e) {
			log.error(String.format("send monitor log error. module: %s", module), e);
		}
	}

	protected SlsClient getSlsClient() {
		return this.slsClient;
	}

}
