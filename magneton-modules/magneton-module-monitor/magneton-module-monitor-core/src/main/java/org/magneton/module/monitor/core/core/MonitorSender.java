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

package org.magneton.module.monitor.core.core;

import org.magneton.foundation.spi.SPI;
import org.magneton.module.monitor.core.core.module.Module;

/**
 * 监控发送器
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see MonitorSenders
 */
@SPI
public interface MonitorSender {

	/**
	 * 发送模型
	 * @apiNote 要根据顺序在多个发送器实例中，进行发送的尝试。如果发送成功，则不再继续发送。如果发送失败，继续使用下一个发送器进行发送。
	 * @param module 模型
	 * @return true：发送成功；false：发送失败
	 */
	boolean send(Module module);

}