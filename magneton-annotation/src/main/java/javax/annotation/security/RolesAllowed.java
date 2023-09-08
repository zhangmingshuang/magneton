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
 * Specifies the list of security roles permitted to access method(s) in an application.
 * The value of the <code>RolesAllowed</code> annotation is a list of security role names.
 * This annotation can be specified on a class or on method(s). Specifying it at a class
 * level means that it applies to all the methods in the class. Specifying it on a method
 * means that it is applicable to that method only. If applied at both the class and
 * methods level, the method value overrides the class value if the two conflict.
 *
 * @since 1.0.2
 * @author gaia
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface RolesAllowed {

	/**
	 * List of roles that are permitted access.
	 * @return list of roles
	 */
	String[] value();

}
