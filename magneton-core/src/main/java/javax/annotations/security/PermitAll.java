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

package javax.annotations.security;

import java.lang.annotation.*;

/**
 * Specifies that all security roles are allowed to invoke the specified method(s) &#8212;
 * i.e., that the specified method(s) are "unchecked". It can be specified on a class or
 * on methods. Specifying it on the class means that it applies to all methods of the
 * class. If specified at the method level, it only affects that method. If the
 * <code>RolesAllowed</code> annotation is specified at the class level and this
 * annotation is applied at the method level, the <code>PermitAll</code> annotation
 * overrides the <code>RolesAllowed</code> annotation for the specified method.
 *
 * @see javax.annotation.security.RolesAllowed
 * @see javax.annotation.security.DenyAll
 *
 * @since 1.0.2
 * @author gaia
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface PermitAll {

}
