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

package org.magneton.framework.design.chain;

import com.google.common.base.MoreObjects;

/**
 * 链节点
 *
 * @author zhangmsh 2022/11/22
 * @since 1.0.0
 */
public abstract class ChainNode<T extends ChainContext> {

	/**
	 * 获取当前执行链的上下文
	 * @return 当前执链的上下文
	 */
	protected T getChainContext() {
		return (T) ChainBus.getChainContext();
	}

	/**
	 * 标记该执行链状态
	 * @param end 执行链状态，如果设置为 {@code true}则表示忽略后续的执行节点。反之则继续
	 */
	protected void setEnd(boolean end) {
		this.getChainContext().setData("$IS_END", end);
	}

	/**
	 * 判断当前执行链是否已结束
	 * @return 如果当前执行链已结束则返回 {@code true}，反之则继续。
	 */
	protected boolean isEnd() {
		return MoreObjects.firstNonNull(this.getChainContext().getData("$IS_END"), Boolean.FALSE);
	}

	/**
	 * 节点执行前置逻辑
	 * @return 返回 {@code true} 表示可执行，调用 {@link #process} ,反之忽略。
	 */
	protected boolean preProcess() {
		return true;
	}

	/**
	 * 节点执行后置逻辑
	 */
	protected void afterProcess() {

	}

	/**
	 * 执行节点
	 */
	public abstract void process();

	/**
	 * 同一个链路所有的节点执行之前调用
	 */
	protected void beforeAll() {
	}

	/**
	 * 同一个链路所有的节点执行完成之后调用
	 */
	protected void afterAll() {
	}

}