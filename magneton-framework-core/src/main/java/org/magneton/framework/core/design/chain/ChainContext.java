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

package org.magneton.framework.core.design.chain;

import lombok.ToString;

import javax.annotation.Nullable;
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

	@Nullable
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

	public <T> boolean setData(String key, T data) {
		return this.putDataMap(key, data);
	}

	@Nullable
	public <T> T getResult() {
		return this.getData("__$$__result_singleton__");
	}

	public <T> void setResult(T data) {
		this.putDataMap("__$$__result_singleton__", data);
	}

	private <T> boolean putDataMap(String key, @Nullable T data) {
		if (data == null) {
			return false;
		}
		this.dataMap.put(key, data);
		return true;
	}

}