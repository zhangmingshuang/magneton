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

import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.MonitorSenders;
import org.magneton.monitor.core.module.ModuleType;
import org.magneton.monitor.core.module.MonitorHeartbeatModule;
import org.magneton.monitor.core.task.ScheduleTask;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 监控心跳监控
 * <p>
 * 该监控器是一个内部的监控器，用来监控当前监控器的线程是否正常运行。
 * <p>
 * 该监控器会每隔30秒钟向监控中心发送一次心跳，如果监控中心在60秒内没有收到心跳，则应该认为监控器线程已经挂掉。
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class MonitorHeartbeatMonitorTask implements ScheduleTask {

	/**
	 * 心跳间隔
	 */
	public static final int HEAD_GAP = 30_000;

	private static final AtomicReference<Long> NEXT_HEARTBEAT = new AtomicReference<>(System.currentTimeMillis());

	private final Biz biz = Biz.of("process", "heartbeat", "monitor");

	@Override
	public String taskId() {
		return "heartbeat";
	}

	@Override
	public void run() {
		try {
			long heartbeat = NEXT_HEARTBEAT.get();
			if (heartbeat > this.current()) {
				return;
			}
			MonitorHeartbeatModule module = new MonitorHeartbeatModule(this.biz);
			module.setModuleType(ModuleType.HEARTBEAT);
			module.setUseExt(false);

			MonitorSenders.getInstance().send(module);

			NEXT_HEARTBEAT.set(System.currentTimeMillis() + this.getGap());
		}
		catch (Throwable e) {
			log.error("[monitor] MonitorHeartbeatMonitor error", e);
		}
	}

	@Override
	public boolean isShutdown() {
		return false;
	}

	@Override
	public void shutdown() {

	}

	private long current() {
		return System.currentTimeMillis();
	}

	protected long getGap() {
		return HEAD_GAP;
	}

}