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

package org.magneton.monitor.core.monitor;

import cn.nascent.tech.gaia.biz.monitor.core.Biz;
import cn.nascent.tech.gaia.biz.monitor.core.MonitorSenders;
import cn.nascent.tech.gaia.biz.monitor.core.module.ModuleType;
import cn.nascent.tech.gaia.biz.monitor.core.module.StatModule;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * 统计监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class OnceStatMonitor {

	private Biz biz;

	private AbstractMonitor abstractMonitor;

	protected OnceStatMonitor(AbstractMonitor abstractMonitor) {
		this.abstractMonitor = abstractMonitor;
	}

	/**
	 * 生产统计
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void product(String serviceProcessor, long productNum, long timeUsed) {
		this.product(serviceProcessor, productNum, timeUsed, 0, null);
	}

	/**
	 * 生产统计
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void product(String serviceProcessor, long productNum, long timeUsed, long timeDelayed) {
		this.product(serviceProcessor, productNum, timeUsed, timeDelayed, null);
	}

	/**
	 * 生产统计
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void product(String serviceProcessor, long productNum, long timeUsed, @Nullable String message) {
		this.product(serviceProcessor, productNum, timeUsed, 0, message);
	}

	/**
	 * 生产统计
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void product(String serviceProcessor, long productNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		try {
			if (Strings.isNullOrEmpty(this.biz.getId())) {
				log.warn("[monitor] bizId is null. Ignore product stat.");
				return;
			}
			if (Strings.isNullOrEmpty(serviceProcessor)) {
				log.warn("[monitor] serviceProcessor is null. Ignore product stat.");
				return;
			}
			StatModule module = new StatModule(this.biz);
			module.setMonitorContext(this.abstractMonitor.getMonitorContext());

			module.setModuleType(ModuleType.PRODUCT);
			module.setMessage(message);

			module.setServiceProcessor(serviceProcessor);
			module.setProductNum(productNum);
			module.setConsumeNum(0);
			module.setTimeUsed(timeUsed);
			module.setTimeDelayed(timeDelayed);

			MonitorSenders.getInstance().send(module);
		}
		finally {
			this.abstractMonitor.shutdown();
		}
	}

	/**
	 * 消费统计
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void consume(String serviceProcessor, long consumeNum, long timeUsed) {
		this.consume(serviceProcessor, consumeNum, timeUsed, 0, null);
	}

	/**
	 * 消费统计
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void consume(String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed) {
		this.consume(serviceProcessor, consumeNum, timeUsed, timeDelayed, null);
	}

	/**
	 * 消费统计
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void consume(String serviceProcessor, long consumeNum, long timeUsed, @Nullable String message) {
		this.consume(serviceProcessor, consumeNum, timeUsed, 0, message);
	}

	/**
	 * 消费统计
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void consume(String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		try {
			if (Strings.isNullOrEmpty(this.biz.getId())) {
				log.warn("[monitor] bizId is null. Ignore consume stat.");
				return;
			}
			if (Strings.isNullOrEmpty(serviceProcessor)) {
				log.warn("[monitor] serviceProcessor is null. Ignore consume stat.");
				return;
			}
			StatModule module = new StatModule(this.biz);
			module.setMonitorContext(this.abstractMonitor.getMonitorContext());

			module.setModuleType(ModuleType.CONSUME);
			module.setMessage(message);
			module.setServiceProcessor(serviceProcessor);

			module.setProductNum(0);
			module.setConsumeNum(consumeNum);
			module.setTimeUsed(timeUsed);
			module.setTimeDelayed(timeDelayed);

			MonitorSenders.getInstance().send(module);
		}
		finally {
			this.abstractMonitor.shutdown();
		}
	}

}