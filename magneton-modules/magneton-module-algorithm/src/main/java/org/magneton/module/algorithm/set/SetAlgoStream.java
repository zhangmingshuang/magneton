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

package org.magneton.module.algorithm.set;

/**
 * 实际算法实现器
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public interface SetAlgoStream<T> {

	/**
	 * 读流
	 * @return 数据
	 */
	SetStream<T> read();

	/**
	 * 随机抽取数据，抽取的数据会从流中移除
	 * @param size 抽取数量，如果数量小于1则返回空集，超过数据量则返回全部数据
	 * @return 抽取数据
	 */
	SetStream<T> randomExtract(long size);

	/**
	 * 清除
	 */
	void clear();

	/**
	 * 是否为空
	 * @return true：空；false：非空
	 */
	boolean isEmpty();

}