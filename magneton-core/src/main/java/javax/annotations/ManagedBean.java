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

package javax.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将一个普通的 Java 类（POJO）标记为受管理的 Bean
 *
 * @since 1.0.2
 * @author gaia
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedBean {

	/**
	 * The name of the Managed Bean. Managed Bean names must be unique within a Jakarta EE
	 * module. For each named Managed Bean, Java EE containers must make available the
	 * following entries in JNDI, using the same naming scheme used for EJB components.
	 * <p>
	 * In the application namespace:
	 * <p>
	 * java:app/&lt;module-name&gt;/&lt;bean-name&gt;
	 * <p>
	 * In the module namespace of the module containing the Managed Bean:
	 * <p>
	 * java:module/&lt;bean-name&gt;
	 * @return The name of the Managed Bean
	 */
	String value() default "";

}
