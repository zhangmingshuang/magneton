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

package org.magneton.module.monitor.core.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundation.spi.SPILoader;
import org.magneton.module.monitor.core.core.module.Module;
import org.magneton.module.monitor.core.core.module.ModuleProcessors;

import java.util.List;

/**
 * 发送执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see MonitorSender
 */
@Slf4j
public class MonitorSenders {

	private static final MonitorSenders INSTANCE = new MonitorSenders();

	/**
	 * 发送器列表
	 */
	private final List<MonitorSender> senders = Lists.newArrayListWithCapacity(12);

	public static MonitorSenders getInstance() {
		return INSTANCE;
	}

	protected MonitorSenders() {
		List<MonitorSender> monitorSenders = SPILoader.loadInstance(MonitorSender.class);
		this.senders.addAll(monitorSenders);
	}

	/**
	 * 注册发送器
	 * @param monitorSender 发送器
	 * @return 是否注册成功
	 */
	public boolean register(MonitorSender monitorSender) {
		Preconditions.checkNotNull(monitorSender, "catSender is null");
		return this.senders.add(monitorSender);
	}

	/**
	 * 注销发送器
	 * @param monitorSender 发送器
	 * @return 是否注销成功
	 */
	public boolean unregister(MonitorSender monitorSender) {
		Preconditions.checkNotNull(monitorSender, "monitorSender is null");
		return this.senders.remove(monitorSender);
	}

	/**
	 * 发送
	 * <p>
	 * 该方法在执行对应的发送之后，会执行相应的加强扩展能力。 包括对模型的加强处理，以及发送动作的前置和后置处理。
	 * @param module 模块
	 */
	public void send(Module module) {
		Preconditions.checkNotNull(module, "module is null");

		try {
			module = this.process(module);
			if (!this.preSend(module)) {
				return;
			}
			for (MonitorSender sender : this.senders) {
				try {
					if (sender.send(module)) {
						this.postSend(sender, module);
						break;
					}
				}
				catch (Exception e) {
					log.error(String.format("send module:%s error.", module), e);
				}
			}
			this.afterSend(module);
			// todo remove会影响使用者自定义的调用方法之前的数据
			MonitorContext.remove();
		}
		catch (Throwable e) {
			log.error("send module error.", e);
		}
	}

	protected Module process(Module module) {
		return ModuleProcessors.getInstance().process(module);
	}

	protected boolean preSend(Module module) {
		return MonitorProcessors.getInstance().preSend(module);
	}

	protected void postSend(MonitorSender sender, Module module) {
		MonitorProcessors.getInstance().postSend(sender, module);
	}

	private void afterSend(Module module) {
		MonitorProcessors.getInstance().afterSend(module);
	}

}
