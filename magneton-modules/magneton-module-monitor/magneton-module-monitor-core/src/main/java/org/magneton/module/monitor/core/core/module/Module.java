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

package org.magneton.module.monitor.core.core.module;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 模型
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public interface Module {

	/**
	 * 服务类型
	 * @return 服务类型
	 */
	String getBizId();

	/**
	 * 使用用例
	 * @return 使用用例
	 */
	@Nullable
	String getUseCase();

	/**
	 * 场景
	 * @return 场景
	 */
	@Nullable
	String getScenario();

	/**
	 * 提示信息
	 * @return 提示信息
	 */
	@Nullable
	String getMessage();

	/**
	 * 模型标识
	 * @return mark
	 */
	ModuleType getModuleType();

	/**
	 * 模型数据
	 * @return data
	 */
	@Nullable
	Map<String, String> getData();

}
