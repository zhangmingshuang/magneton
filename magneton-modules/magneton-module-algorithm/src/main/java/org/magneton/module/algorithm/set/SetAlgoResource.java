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

package org.magneton.module.algorithm.set;

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 算法资源
 *
 * @author zhangmsh 2022/10/27
 * @since 2.1.0
 */
public interface SetAlgoResource<T, S extends SetAlgoStream<T>> {

	/**
	 * 添加要去重的资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> distinct(Set<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.distinct(new SetAlgoFile(Lists.newArrayList(files)));
	}

	/**
	 * 添加要去重的资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> distinct(List<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.distinct(new SetAlgoFile(files));
	}

	/**
	 * 添加要去重的资源
	 * @param file 配置
	 * @return this
	 */
	SetAlgoResource<T, S> distinct(SetAlgoFile file);

	/**
	 * 去重
	 * @param data 值
	 * @return this
	 */
	default SetAlgoResource<T, S> distinct(T... data) {
		return this.distinct(SetAlgoData.of(data));
	}

	/**
	 * 去重
	 * @param data 值
	 * @return this
	 */
	SetAlgoResource<T, S> distinct(SetAlgoData<T> data);

	/**
	 * 去重
	 * @param stream 流
	 * @return this
	 */
	SetAlgoResource<T, S> distinct(SetStream<T> stream);

	/**
	 * 添加要排除的资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> exclude(Set<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.exclude(new SetAlgoFile(Lists.newArrayList(files)));
	}

	/**
	 * 添加要排除的资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> exclude(List<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.exclude(new SetAlgoFile(files));
	}

	/**
	 * 添加要排除的资源
	 * @param files 配置
	 * @return this
	 */
	SetAlgoResource<T, S> exclude(SetAlgoFile files);

	/**
	 * 排除
	 * @param data 值
	 * @return this
	 */
	default SetAlgoResource<T, S> exclude(T... data) {
		return this.exclude(SetAlgoData.of(data));
	}

	/**
	 * 排除
	 * @param data 值
	 * @return this
	 */
	SetAlgoResource<T, S> exclude(SetAlgoData<T> data);

	/**
	 * 去除
	 * @param stream 流
	 * @return this
	 */
	SetAlgoResource<T, S> exclude(SetStream<T> stream);

	/**
	 * 添加交集资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> intersect(Set<String> files) {
		Preconditions.checkNotNull(files, "file");
		return this.intersect(new SetAlgoFile(Lists.newArrayList(files)));
	}

	/**
	 * 添加交集资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> intersect(List<String> files) {
		Preconditions.checkNotNull(files, "file");
		return this.intersect(new SetAlgoFile(files));
	}

	/**
	 * 添加交集资源
	 * @param files 配置
	 * @return this
	 */
	SetAlgoResource<T, S> intersect(SetAlgoFile files);

	/**
	 * 交集
	 * @param data 值
	 * @return this
	 */
	default SetAlgoResource<T, S> intersect(T... data) {
		return this.intersect(SetAlgoData.of(data));
	}

	/**
	 * 交集
	 * @param data 值
	 * @return this
	 */
	SetAlgoResource<T, S> intersect(SetAlgoData<T> data);

	/**
	 * 交集
	 * @param stream 流
	 * @return this
	 */
	SetAlgoResource<T, S> intersect(SetStream<T> stream);

	/**
	 * 添加并集资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> union(Set<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.union(new SetAlgoFile(Lists.newArrayList(files)));
	}

	/**
	 * 添加并集资源
	 * @param files 文件列表
	 * @return this
	 */
	default SetAlgoResource<T, S> union(List<String> files) {
		Preconditions.checkNotNull(files, "files");
		return this.union(new SetAlgoFile(files));
	}

	/**
	 * 添加并集资源
	 * @param file 配置
	 * @return this
	 */
	SetAlgoResource<T, S> union(SetAlgoFile file);

	/**
	 * 并集
	 * @param data 值
	 * @return this
	 */
	default SetAlgoResource<T, S> union(T... data) {
		return this.union(SetAlgoData.of(data));
	}

	/**
	 * 并集
	 * @param data 值
	 * @return this
	 */
	SetAlgoResource<T, S> union(SetAlgoData<T> data);

	/**
	 * 并集
	 * @param stream 流
	 * @return this
	 */
	SetAlgoResource<T, S> union(SetStream<T> stream);

	/**
	 * 转换流
	 * @return 数据流
	 */
	S stream();

}
