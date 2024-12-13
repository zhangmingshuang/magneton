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

import cn.nascent.tech.gaia.biz.log.sls.SlsConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * sls 配置
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@Component
@ToString
@ConfigurationProperties(prefix = "gaia.biz.monitor")
public class SlsMonitorProperty {

	/**
	 * SLS配置
	 * <p>
	 * key=profile, value=cfg
	 */
	@NestedConfigurationProperty
	private Map<String, SlsConfig> sls;

}
