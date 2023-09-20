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

package org.magneton.module.monitor.core.core.monitor;

import com.google.common.base.Preconditions;
import org.magneton.module.monitor.core.core.Biz;
import org.magneton.module.monitor.core.core.MonitorSenders;
import org.magneton.module.monitor.core.core.module.ModuleType;
import org.magneton.module.monitor.core.core.module.StatModule;

import javax.annotation.Nullable;

/**
 * 统计监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class StatMonitor {

	/**
	 * Singleton instance.
	 */
	private static final StatMonitor INSTANCE = new StatMonitor();

	public static StatMonitor getInstance() {
		return INSTANCE;
	}

	protected StatMonitor() {
	}

	/**
	 * 生产统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void product(String bizId, String serviceProcessor, long productNum, long timeUsed) {
		this.product(bizId, null, null, serviceProcessor, productNum, timeUsed, 0, null);
	}

	/**
	 * 生产统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void product(String bizId, String serviceProcessor, long productNum, long timeUsed, long timeDelayed) {
		this.product(bizId, null, null, serviceProcessor, productNum, timeUsed, timeDelayed, null);
	}

	/**
	 * 生产统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void product(String bizId, String serviceProcessor, long productNum, long timeUsed,
			@Nullable String message) {
		this.product(bizId, null, null, serviceProcessor, productNum, timeUsed, 0, message);
	}

	/**
	 * 生产统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void product(String bizId, String serviceProcessor, long productNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		this.product(bizId, null, null, serviceProcessor, productNum, timeUsed, timeDelayed, message);
	}

	/**
	 * 生产统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void product(Biz biz, String serviceProcessor, long productNum, long timeUsed) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.product(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, productNum, timeUsed, 0, null);
	}

	/**
	 * 生产统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void product(Biz biz, String serviceProcessor, long productNum, long timeUsed, long timeDelayed) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.product(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, productNum, timeUsed,
				timeDelayed, null);
	}

	/**
	 * 生产统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void product(Biz biz, String serviceProcessor, long productNum, long timeUsed, @Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.product(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, productNum, timeUsed, 0,
				message);
	}

	/**
	 * 生产统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void product(Biz biz, String serviceProcessor, long productNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.product(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, productNum, timeUsed,
				timeDelayed, message);
	}

	/**
	 * 生产统计
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param serviceProcessor 处理服务名称
	 * @param productNum 生产数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	private void product(String bizId, @Nullable String useCase, @Nullable String scenario,
			// product stat infos.
			String serviceProcessor, long productNum, long timeUsed, long timeDelayed, @Nullable String message) {
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkNotNull(serviceProcessor, "serviceProcessor is null");
		Preconditions.checkArgument(productNum > 0, "productNum must be greater than 0");
		Preconditions.checkArgument(timeUsed >= 0, "time used must be greater than or equal to 0");

		StatModule module = new StatModule();
		module.setBizId(bizId);
		module.setUseCase(useCase);
		module.setScenario(scenario);
		module.setModuleType(ModuleType.PRODUCT);
		module.setMessage(message);

		module.setServiceProcessor(serviceProcessor);
		module.setProductNum(productNum);
		module.setConsumeNum(0);
		module.setTimeUsed(timeUsed);
		module.setTimeDelayed(timeDelayed);

		MonitorSenders.getInstance().send(module);
	}

	/**
	 * 消费统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void consume(String bizId, String serviceProcessor, long consumeNum, long timeUsed) {
		this.consume(bizId, null, null, serviceProcessor, consumeNum, timeUsed, 0, null);
	}

	/**
	 * 消费统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void consume(String bizId, String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed) {
		this.consume(bizId, null, null, serviceProcessor, consumeNum, timeUsed, timeDelayed, null);
	}

	/**
	 * 消费统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void consume(String bizId, String serviceProcessor, long consumeNum, long timeUsed,
			@Nullable String message) {
		this.consume(bizId, null, null, serviceProcessor, consumeNum, timeUsed, 0, message);
	}

	/**
	 * 消费统计
	 * @param bizId 业务ID
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void consume(String bizId, String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		this.consume(bizId, null, null, serviceProcessor, consumeNum, timeUsed, timeDelayed, message);
	}

	/**
	 * 消费统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 */
	public void consume(Biz biz, String serviceProcessor, long consumeNum, long timeUsed) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.consume(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, consumeNum, timeUsed, 0, null);
	}

	/**
	 * 消费统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 */
	public void consume(Biz biz, String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.consume(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, consumeNum, timeUsed,
				timeDelayed, null);
	}

	/**
	 * 消费统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void consume(Biz biz, String serviceProcessor, long consumeNum, long timeUsed, @Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.consume(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, consumeNum, timeUsed, 0,
				message);
	}

	/**
	 * 消费统计
	 * @param biz 业务
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	public void consume(Biz biz, String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed,
			@Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.consume(biz.getId(), biz.getUseCase(), biz.getScenario(), serviceProcessor, consumeNum, timeUsed,
				timeDelayed, message);
	}

	/**
	 * 消费统计
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param serviceProcessor 处理服务名称
	 * @param consumeNum 消费数量
	 * @param timeUsed 耗时，毫秒
	 * @param timeDelayed 延迟时间，毫秒
	 * @param message 提示信息
	 */
	private void consume(String bizId, @Nullable String useCase, @Nullable String scenario,
			// consume stat infos.
			String serviceProcessor, long consumeNum, long timeUsed, long timeDelayed, @Nullable String message) {
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkNotNull(serviceProcessor, "serviceProcessor is null");
		Preconditions.checkArgument(consumeNum > 0, "consumeNum must be greater than 0");
		Preconditions.checkArgument(timeUsed >= 0, "time used must be greater than or equal to 0");

		StatModule module = new StatModule();
		module.setBizId(bizId);
		module.setUseCase(useCase);
		module.setScenario(scenario);
		module.setModuleType(ModuleType.CONSUME);
		module.setMessage(message);
		module.setServiceProcessor(serviceProcessor);

		module.setProductNum(0);
		module.setConsumeNum(consumeNum);
		module.setTimeUsed(timeUsed);
		module.setTimeDelayed(timeDelayed);

		MonitorSenders.getInstance().send(module);
	}

}
