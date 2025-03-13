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

package javax.annotation.security;

import java.lang.annotation.*;

/**
 * Defines the identity of the application during execution. This allows developers to
 * execute an application under a particular role. The role must map to the user / group
 * information in the container's security realm. Its value is the name of a security
 * role.
 *
 * @since 1.0.2
 * @author gaia
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RunAs {

	/**
	 * Name of a security role.
	 * @return the name of a security role
	 */
	String value();

}
