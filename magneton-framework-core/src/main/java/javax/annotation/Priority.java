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

package javax.annotation;

import java.lang.annotation.*;

/**
 * The <code>Priority</code> annotation can be applied to classes or parameters to
 * indicate in what order they should be used. The effect of using the
 * <code>Priority</code> annotation in any particular instance is defined by other
 * specifications that define the use of a specific class.
 * <p>
 * For example, the Interceptors specification defines the use of priorities on
 * interceptors to control the order in which interceptors are called.
 * </p>
 * <p>
 * Priority values should generally be non-negative, with negative values reserved for
 * special meanings such as "undefined" or "not specified". A specification that defines
 * use of the <code>Priority</code> annotation may define the range of allowed priorities
 * and any priority values with special meaning.
 * </p>
 *
 * @since 1.0.2
 * @author gaia
 */
@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Priority {

	/**
	 * The priority value.
	 * @return the priority value
	 */
	int value();

}
