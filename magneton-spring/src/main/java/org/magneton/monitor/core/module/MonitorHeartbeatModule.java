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

import lombok.ToString;
import org.magneton.monitor.core.Biz;

import java.util.Map;

/**
 * 监控心跳模块
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@ToString
public class MonitorHeartbeatModule extends BaseModule<MonitorHeartbeatModule> implements InnerModule {

	public MonitorHeartbeatModule(Biz biz) {
		super(biz);
	}

	@Override
	public Map<String, String> getData() {
		return null;
	}

}
