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

package org.magneton.monitor;
 
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.monitor.OnceMonitor;
import org.magneton.monitor.core.monitor.SessionMonitor;

/**
 * 监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class Monitor {

	private Monitor() {
		// private
	}

	public static OnceMonitor once(String bizId) {
		if (Strings.isNullOrEmpty(bizId)) {
			log.warn("[monitor] bizId is null. Skip once stat.");
		}
		return new OnceMonitor(Biz.of(bizId));
	}

	public static OnceMonitor once(Biz biz) {
		if (biz == null || Strings.isNullOrEmpty(biz.getId())) {
			log.warn("[monitor] bizId is null. Skip once stat.");
		}
		return new OnceMonitor(biz);
	}

	public static SessionMonitor session() {
		return new SessionMonitor();
	}

}