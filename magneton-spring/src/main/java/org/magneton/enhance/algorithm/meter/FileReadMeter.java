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

package org.magneton.enhance.algorithm.meter;

import java.nio.file.Path;

/**
 * 文件读取指标
 *
 * @author zhangmsh 2022/10/25
 * @since 2.1.0
 */
public interface FileReadMeter extends Meter {

	/**
	 * 预读
	 * @param file 文件
	 * @return 如果返回{@code true}表示继续，否则表示中断
	 */
	boolean preRead(Path file);

	/**
	 * 增加读取行数
	 * @param file 文件地址
	 */
	void incrReadCount(Path file);

	/**
	 * 增加跳过行数
	 * @param file 文件地址
	 */
	void incrSkipCount(Path file);

	/**
	 * 增加忽略行数
	 * @param file 文件地址
	 * @param data 忽略数据
	 * @param <T> T
	 */
	<T> void ignoreCount(Path file, Object data);

}
