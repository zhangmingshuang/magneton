/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.module.algorithm.set.chain;

/**
 * 链路数据
 *
 * @author zhangmsh 2022/10/27
 * @since 2.1.0
 */
public interface ChainData<T, D> {

	/**
	 * 是否存在
	 * @param val 值
	 * @return 如果存在返回 {@code true}，否则返回 {@code false}.
	 */
	boolean contains(T val);

	/**
	 * 添加值
	 * @param val 值
	 */
	void add(T val);

	/**
	 * 删除值
	 * @param val 值
	 */
	void remove(T val);

	/**
	 * 判断空
	 * @return 如果为空则返回 {@code true}，反之返回 {@code false}.
	 */
	boolean isEmpty();

	/**
	 * 获取当前数据
	 * @return 当前数据
	 */
	D getData();

	/**
	 * 获取数据大小
	 * @return 数据大小
	 */
	long size();

	/**
	 * 清除数据
	 */
	void clear();

	/**
	 * 随机抽出写入到文件
	 *
	 * 注：抽出的数据将从数据集中移出
	 * @param size 抽出数量
	 * @return 写入文件的数量
	 */
	D randomExtract(long size);

}
