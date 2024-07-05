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

package org.magneton.foundiation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * 參數處理器.
 *
 * @apiNote 用来处理获取环境变量、启动参数等相应的配置参数信息。
 * @author zhangmsh 31/03/2022
 * @since 2.0.8
 */
@ToString
public class RuntimeArgs implements Operable, Operation {

	private String value;

	private Getter getter;

	private RuntimeArgs(Getter getter) {
		this.getter = Preconditions.checkNotNull(getter, "getter");
	}

	@Override
	public Operation of(String arg) {
		this.value = this.getter.get(arg);
		return this;
	}

	@Override
	@Nullable
	public String get() {
		return this.value;
	}

	@Override
	public Operation or(String arg) {
		return this.value != null ? this : this.of(arg);
	}

	@Nullable
	@Override
	public String orElse(String arg) {
		return this.value != null ? this.value : this.getter.get(arg);
	}

	@Override
	public String orElse(String arg, String defaultValue) {
		return MoreObjects.firstNonNull(this.orElse(arg), defaultValue);
	}

	public static Operable from(Getter getter) {
		return new RuntimeArgs(getter);
	}

	public static Operation env(String arg) {
		return from(Getters.ENV).of(arg);
	}

	public static Operation property(String arg) {
		return from(Getters.PROPERTY).of(arg);
	}

	public static Operation sys(String arg) {
		return from(Getters.MIXED).of(arg);
	}

	interface Getter {

		/**
		 * 读取
		 * @param arg 参数名
		 * @return 参数值
		 */
		@Nullable
		String get(String arg);

	}

	enum Getters implements Getter {

		/**
		 * 环境变量读取
		 */
		ENV {
			@Nullable
			@Override
			public String get(String arg) {
				return System.getenv(arg);
			}
		},
		/**
		 * 系统属性读取
		 */
		PROPERTY {
			@Nullable
			@Override
			public String get(String arg) {
				return System.getProperty(arg);
			}
		},
		/**
		 * 混合读取，优先读取环境变量，再读取系统属性
		 */
		MIXED {
			@Nullable
			@Override
			public String get(String arg) {
				String env = ENV.get(arg);
				return Strings.isNullOrEmpty(env) ? PROPERTY.get(arg) : env;
			}
		};

	}

}