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
import cn.nascent.tech.gaia.biz.monitor.support.Switcher;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Abstract monitor.
 *
 * @author zhangmsh
 * @since 4.0.0
 */
@Slf4j
public abstract class AbstractMonitor implements IMonitor {

	protected final Switcher switcher = new Switcher();

	protected final Biz biz;

	@Nullable
	private MonitorContext monitorContext;

	@Nullable
	private Supplier<MonitorContext> monitorContextSupplier;

	protected AbstractMonitor(Biz biz) {
		this.biz = biz;
	}

	@Override
	public void attachContext(MonitorContext monitorContext) {
		this.monitorContext = monitorContext;
	}

	@Override
	public void attachContext(Supplier<MonitorContext> supplier) {
		this.monitorContextSupplier = supplier;
	}

	public MonitorContext getMonitorContext() {
		if (this.monitorContext == null && this.monitorContextSupplier == null) {
			return null;
		}
		if (this.monitorContextSupplier == null) {
			return this.monitorContext;
		}
		MonitorContext polymericMonitorContext = new MonitorContext();
		if (this.monitorContext != null) {
			polymericMonitorContext.putAll(this.monitorContext.getContext());
		}
		MonitorContext supplier = this.monitorContextSupplier.get();
		if (supplier != null) {
			polymericMonitorContext.putAll(supplier.getContext());
		}
		return polymericMonitorContext;
	}

	public void shutdown() {
		this.switcher.shutdownNow(() -> {
			if (this.monitorContext != null) {
				this.monitorContext.clear();
			}
			if (this.monitorContextSupplier != null) {
				this.monitorContextSupplier = null;
			}
		});
	}

	public boolean isShutdown() {
		return this.switcher.isShutdown();
	}

	public Biz getBiz() {
		return this.biz;
	}

}
