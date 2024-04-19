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

package org.magneton.framework.design.chain;

import com.google.common.base.Preconditions;
import lombok.ToString;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 链上下文
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@ToString
public class ChainContext {

	private final ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

	public boolean hasData(String key) {
		return this.dataMap.containsKey(key);
	}

	public <T> T getData(String key) {
		return (T) this.dataMap.get(key);
	}

	public String getData() {
		if (this.dataMap.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		this.dataMap.forEach((k, v) -> builder.append(k).append("=").append(v).append(", "));
		return builder.substring(0, builder.length() - 2);
	}

	public <T> void setData(String key, T data) {
		this.putDataMap(key, data);
	}

	private <T> void putDataMap(String key, T data) {
		Preconditions.checkNotNull(data, "data can't accept null param");
		this.dataMap.put(key, data);
	}

}