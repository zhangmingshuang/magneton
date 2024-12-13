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

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trigger Buffer
 *
 * @author zhangmsh
 * @since 4.0.0
 * @see TriggerBufferTask
 * @see SessionMonitor
 */

public class TriggerBuffer {

	/**
	 * 所属sessionId，一个sessionId对应一个TriggerBuffer
	 * @see SessionMonitor
	 */
	@Getter
	private final String sessionId;

	private final AtomicBoolean shutdown = new AtomicBoolean(false);

	/**
	 * 注释的触发器
	 */
	@Getter
	private final Map<String, ScheduleTrigger> triggers = new ConcurrentHashMap<>((int) (1.3 * 1024));

	public TriggerBuffer(String sessionId) {
		this.sessionId = sessionId;
		TriggerBufferTask.add(this);
	}

	public boolean add(ScheduleTrigger scheduleTrigger) {
		if (this.shutdown.get()) {
			return false;
		}
		this.triggers.put(scheduleTrigger.getTriggerId(), scheduleTrigger);
		return true;
	}

	public boolean exist(String triggerId) {
		return this.triggers.containsKey(triggerId);
	}

	@Nullable
	public ScheduleTrigger get(String triggerId) {
		return this.triggers.get(triggerId);
	}

	public void remove(ScheduleTrigger scheduleTrigger) {
		scheduleTrigger.getData().setRemoved(true);
		TriggerBufferTask.removeDataIfPresent(scheduleTrigger.getData());
		this.triggers.remove(scheduleTrigger.getTriggerId());
	}

	public boolean shutdown() {
		if (this.shutdown.compareAndSet(false, true)) {
			TriggerBufferTask.remove(this);

			this.triggers.forEach((k, v) -> v.shutdownNow());
			this.triggers.clear();
			return true;
		}
		return false;
	}

	public int size() {
		return this.triggers.size();
	}

	public boolean isShutdown() {
		return this.shutdown.get();
	}

}
