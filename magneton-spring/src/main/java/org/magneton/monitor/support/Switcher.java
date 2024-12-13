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

package org.magneton.monitor.support;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 状态交换器
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class Switcher {

	private static final int INIT = 0;

	private static final int START = 1;

	private static final int STOP = 2;

	private static final int SHUTDOWN = 3;

	private volatile boolean shuttingDown = false;

	private final AtomicReference<Integer> state = new AtomicReference<>(INIT);

	public boolean start() {
		return this.start(null);
	}

	public boolean start(SwitcherInvoker invoker) {
		if (this.state.get() == SHUTDOWN || this.shuttingDown) {
			return false;
		}
		if (this.state.compareAndSet(INIT, START)) {
			this.doInvoker(invoker);
			return true;
		}
		return false;
	}

	private void doInvoker(SwitcherInvoker invoker) {
		if (invoker != null) {
			invoker.onSwitch();
		}
	}

	public boolean isStarted() {
		return this.state.get() == START;
	}

	public boolean stop() {
		return this.stop(null);
	}

	public boolean stop(SwitcherInvoker switcherInvoker) {
		if (this.state.get() == SHUTDOWN || this.shuttingDown) {
			return false;
		}
		if (this.state.compareAndSet(START, STOP)) {
			this.doInvoker(switcherInvoker);
			return true;
		}
		return false;
	}

	public boolean isStopped() {
		return this.state.get() == STOP;
	}

	public void shutdownNow() {
		this.shutdownNow(null);
	}

	public void shutdownNow(SwitcherInvoker switcherInvoker) {
		this.shuttingDown = true;
		while (true) {
			Integer now = this.state.get();
			if (now == SHUTDOWN) {
				break;
			}
			if (this.state.compareAndSet(now, SHUTDOWN)) {
				this.doInvoker(switcherInvoker);
				break;
			}
		}
	}

	public boolean isShutdown() {
		return this.state.get() == SHUTDOWN;
	}

}
