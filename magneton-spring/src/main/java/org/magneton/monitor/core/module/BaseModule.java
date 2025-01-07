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

package org.magneton.monitor.core.module;

import cn.nascent.tech.gaia.biz.monitor.core.Biz;
import cn.nascent.tech.gaia.biz.monitor.core.monitor.MonitorContext;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * 通用模型
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public abstract class BaseModule<T extends BaseModule<T>> implements Module {

	/**
	 * 监控上下文
	 */
	@Nullable
	private MonitorContext monitorContext;

	/**
	 * 业务
	 */
	@Getter
	private Biz biz;

	/**
	 * 提示信息
	 */
	@Nullable
	private String message;

	/**
	 * 模块类型
	 */
	private ModuleType moduleType;

	private boolean useExt = true;

	public BaseModule(Biz biz) {
		this.biz = biz;
	}

	@Override
	@Nullable
	public String getMessage() {
		return this.message;
	}

	@Override
	public ModuleType getModuleType() {
		return this.moduleType;
	}

	public T setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
		return (T) this;
	}

	public T setMessage(String message) {
		this.message = message;
		return (T) this;
	}

	public T setUseExt(boolean useExt) {
		this.useExt = useExt;
		return (T) this;
	}

	@Override
	public boolean isUseExt() {
		return this.useExt;
	}

	public void setMonitorContext(MonitorContext monitorContext) {
		this.monitorContext = monitorContext;
	}

	@Override
	public MonitorContext getMonitorContext() {
		return this.monitorContext;
	}

}
