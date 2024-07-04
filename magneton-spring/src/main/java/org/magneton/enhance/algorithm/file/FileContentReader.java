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

package org.magneton.enhance.algorithm.file;

import java.io.IOException;

/**
 * 文件内容读取器
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public interface FileContentReader {

	/**
	 * 是否有下一行
	 * @return 如果还有下一行数据，返回 {@code true}，否则返回 {@code false}.
	 * @throws IOException IO异常
	 */
	boolean hasNext() throws IOException;

	/**
	 * 下一行数据
	 * @return 下一行数据
	 * @throws IOException IO异常
	 */
	String next() throws IOException;

	/**
	 * 关闭
	 */
	void close();

}
