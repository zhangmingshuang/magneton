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

package org.magneton.test.mock;

/**
 * 不支持异常.
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class UnsupportedException extends RuntimeException {

	/**
	 * 异常单例
	 */
	public static final UnsupportedException INSTANCE = new UnsupportedException();

	private static final long serialVersionUID = -2657719512352019241L;

	public UnsupportedException() {
		super("暂不支持该方法");
	}

}
