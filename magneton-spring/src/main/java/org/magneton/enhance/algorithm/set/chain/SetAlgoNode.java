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

package org.magneton.enhance.algorithm.set.chain;

/**
 * 算法节点
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public interface SetAlgoNode<T, D> {

	/**
	 * 判断数据是否可用
	 * @param chainData 链路数据
	 * @param data 当前数据
	 * @return 如果可以使用,则返回 {@code true}，反之返回 {@code false}.
	 */
	boolean useable(ChainData<T, D> chainData, String data);

	/**
	 * 每个节点切换之前
	 * @param chainData 链路数据
	 * @param node 完成的节点
	 */
	default void eachNode(ChainData<T, D> chainData, SetAlgoNode<T, D> node) {

	}

	/**
	 * 每个流式处理之后
	 * @param chainData 链跟数据
	 * @param node 处理的节点
	 */
	default void eachStream(ChainData<T, D> chainData, SetAlgoNode<T, D> node) {

	}

	/**
	 * 清除
	 */
	default void clear() {

	}

}
