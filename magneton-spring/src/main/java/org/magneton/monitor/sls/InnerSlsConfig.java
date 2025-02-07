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

package org.magneton.monitor.sls;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.magneton.log.SlsConfig;
import org.magneton.monitor.SlsMonitorAutoConfiguration;
import org.magneton.monitor.core.module.ModuleType;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * sls 内置配置，用于外部配置没有配置时，使用内置配置
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Component
public class InnerSlsConfig {

	/**
	 * endpoint
	 */
	private static final String ENDPOINT = "cn-zhangjiakou.log.aliyuncs.com";

	/**
	 * ts
	 */
	private static final char[] TS_AK = new char[] { 'O', 'W', 'D', 'L', '4', 'J', 'K', 'G', 'x', 'n', 'n', '7', 'H',
			'Y', 'p', '8', 'V', 'B', 'Z', 'b', 'Z', '1', '5', 'u' };

	/**
	 * ts
	 */
	private static final char[] TS_SK = new char[] { '6', 'A', 'y', 'Q', 'M', '1', 'G', 'h', 'H', 'l', 'C', 'H', '6',
			'N', 'M', 'q', '1', 'S', '6', 'L', '6', 'g', 'z', '6', 'C', 'N', 'W', 'M', 'v', 'N' };

	/**
	 * p
	 */
	private static final char[] P_AK = new char[] { 'Q', 'Y', 'F', 'N', '5', 'y', 'O', 'w', 'R', '8', 'L', 'H', 'Q',
			'6', 'e', 'z', 'd', 'm', 't', 'c', 'W', 'j', 'Y', 'L' };

	/**
	 * p
	 */
	private static final char[] P_SK = new char[] { 'h', 'M', 'w', 'C', 'e', 'J', 'q', 'f', 'g', 'S', '1', 'X', 'N',
			'1', 'Z', 'U', 'q', 'i', 'X', 'z', 'n', 'c', 'S', 'c', '8', '7', '5', 'N', 'P', 'g' };

	/**
	 * 单例
	 */
	private static final InnerSlsConfig INSTANCE = new InnerSlsConfig();

	/**
	 * sls 内置配置
	 * @return sls 内置配置
	 */
	public static InnerSlsConfig getInstance() {
		return INSTANCE;
	}

	private final Map<String, SlsConfig> slsConfigs = Maps.newHashMapWithExpectedSize(2);

	private InnerSlsConfig() {
		this.init();
	}

	@Nullable
	public SlsConfig getSlsConfig(String profile) {
		if (Strings.isNullOrEmpty(profile)) {
			profile = "*";
		}
		SlsConfig config = this.slsConfigs.get(profile);
		if (Objects.isNull(config)) {
			return this.slsConfigs.get("*");
		}
		return config;
	}

	private void init() {
		this.initDefault();
		this.initPre();
		this.initProd();
	}

	private void initProd() {
		if (!Profiles.PROD.equals(SlsMonitorAutoConfiguration.getProfile())) {
			return;
		}
		SlsConfig slsConfig = new SlsConfig();
		slsConfig.setEndpoint(ENDPOINT);
		slsConfig.setAccessKey(new String(E.b(P_AK, 5)));
		slsConfig.setAccessSecret(new String(E.b(P_SK, 6)));

		SlsConfig.Config tsConfig = new SlsConfig.Config();
		tsConfig.setProject("prod-monitor");
		tsConfig.setLogStore("service-monitor");
		slsConfig.setProducer(tsConfig);

		Map<String, SlsConfig.Config> bizProducer = Maps.newHashMapWithExpectedSize(1);
		SlsConfig.Config tsBizConfig = new SlsConfig.Config();
		tsBizConfig.setProject("prod-monitor");
		tsBizConfig.setLogStore("business-monitor");
		bizProducer.put(ModuleType.CONSUME.getType(), tsBizConfig);
		slsConfig.setBizProducer(bizProducer);
		this.slsConfigs.put(Profiles.PROD, slsConfig);
	}

	private void initPre() {
		if (!Profiles.PRE.equals(SlsMonitorAutoConfiguration.getProfile())) {
			return;
		}
		SlsConfig slsConfig = new SlsConfig();
		slsConfig.setEndpoint(ENDPOINT);
		slsConfig.setAccessKey(new String(E.b(TS_AK, 3)));
		slsConfig.setAccessSecret(new String(E.b(TS_SK, 4)));

		SlsConfig.Config preConfig = new SlsConfig.Config();
		preConfig.setProject("hd3-test");
		preConfig.setLogStore("uat-svc-monitor");
		slsConfig.setProducer(preConfig);

		Map<String, SlsConfig.Config> bizProducer = Maps.newHashMapWithExpectedSize(1);
		SlsConfig.Config tsBizConfig = new SlsConfig.Config();
		tsBizConfig.setProject("hd3-test");
		tsBizConfig.setLogStore("uat-biz-monitor");
		bizProducer.put(ModuleType.CONSUME.getType(), tsBizConfig);
		slsConfig.setBizProducer(bizProducer);
		this.slsConfigs.put(Profiles.PRE, slsConfig);
	}

	private void initDefault() {
		SlsConfig slsConfig = new SlsConfig();
		slsConfig.setEndpoint(ENDPOINT);
		slsConfig.setAccessKey(new String(E.b(TS_AK, 3)));
		slsConfig.setAccessSecret(new String(E.b(TS_SK, 4)));

		SlsConfig.Config tsConfig = new SlsConfig.Config();
		tsConfig.setProject("test");
		tsConfig.setLogStore("test-monitor");
		slsConfig.setProducer(tsConfig);

		Map<String, SlsConfig.Config> bizProducer = Maps.newHashMapWithExpectedSize(1);
		SlsConfig.Config tsBizConfig = new SlsConfig.Config();
		tsBizConfig.setProject("test");
		tsBizConfig.setLogStore("test-monitor");
		bizProducer.put(ModuleType.CONSUME.getType(), tsBizConfig);
		slsConfig.setBizProducer(bizProducer);
		// 默认情况下，所有的模块都使用 tsConfig
		this.slsConfigs.put("*", slsConfig);
	}

	protected static class E {

		protected static char[] a(char[] ics, int si) {
			char[] cs = new char[ics.length];
			for (int i = 0; i < ics.length; i++) {
				char c = ics[i];
				if (c >= 'A' && c <= 'Z') {
					cs[i] = (char) ((c - 'A' + si) % 26 + 'A');
				}
				else if (c >= 'a' && c <= 'z') {
					cs[i] = (char) ((c - 'a' + si) % 26 + 'a');
				}
				else {
					cs[i] = c;
				}
			}
			return cs;
		}

		protected static char[] b(char[] ecs, int is) {
			char[] dcs = new char[ecs.length];
			for (int i = 0; i < ecs.length; i++) {
				char c = ecs[i];
				if (c >= 'A' && c <= 'Z') {
					dcs[i] = (char) ((c - 'A' - is + 26) % 26 + 'A');
				}
				else if (c >= 'a' && c <= 'z') {
					dcs[i] = (char) ((c - 'a' - is + 26) % 26 + 'a');
				}
				else {
					dcs[i] = c;
				}
			}
			return dcs;
		}

	}

}
