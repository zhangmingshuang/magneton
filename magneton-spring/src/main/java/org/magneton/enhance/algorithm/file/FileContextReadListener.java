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

import java.nio.file.Path;

/**
 * 行文件内容监听器
 *
 * @author zhangmsh 2022/10/26
 * @since 2.1.0
 */
public interface FileContextReadListener {

	/**
	 * 加载行
	 * @param path 读取中的文件
	 * @param data 当前加载行数据
	 * @return 是否继续，如果返回{@code true}则继续加载，否则中断。
	 */
	boolean onRead(Path path, String data);

}
