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

import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

/**
 * 输出Stream
 *
 * @author zhangmsh 2022/10/28
 * @since 2.1.0
 */
public interface SetStream<T> {

	/**
	 * 获取流数据
	 * @return 数据
	 */
	List<T> fetch();

	/**
	 * 迭代器
	 * @return 迭代器
	 */
	Iterator<T> iterator();

	/**
	 * 循环获取
	 * @param consumer 消费者
	 */
	void forEach(Consumer<T> consumer);

	/**
	 * 写流
	 * @param path 文件地址
	 * @return 写入数量
	 */
	default long write(Path path) {
		return this.write(path, Collections.emptyList());
	}

	/**
	 * 写流
	 * @param path 文件地址
	 * @param headers 文件头
	 * @return 写入数量
	 */
	default long write(Path path, String... headers) {
		return this.write(path, Lists.newArrayList(headers));
	}

	/**
	 * 写流
	 * @param path 文件地址
	 * @param headers 文件头
	 * @return 写入数量
	 */
	long write(Path path, List<String> headers);

	/**
	 * 流大小
	 * @return 流大小
	 */
	long size();

	boolean isEmpty();

	/**
	 * 清除
	 */
	void clear();

}
