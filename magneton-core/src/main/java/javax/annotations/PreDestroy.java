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

import java.lang.annotation.*;

/**
 * The <code>PreDestroy</code> annotation is used on a method as a callback notification
 * to signal that the instance is in the process of being removed by the container. The
 * method annotated with <code>PreDestroy</code> is typically used to release resources
 * that it has been holding. This annotation must be supported by all container-managed
 * objects that support the use of the <code>PostConstruct</code> annotation except the
 * Jakarta EE application client. The method on which the <code>PreDestroy</code>
 * annotation is applied must fulfill all of the following criteria:
 * <ul>
 * <li>The method must not have any parameters except in the case of interceptors in which
 * case it takes an <code>InvocationContext</code> object as defined by the Interceptors
 * specification.</li>
 * <li>The method defined on an interceptor class or superclass of an interceptor class
 * must have one of the following signatures:
 * <p>
 * void &#060;METHOD&#062;(InvocationContext)
 * <p>
 * Object &#060;METHOD&#062;(InvocationContext) throws Exception
 * <p>
 * <i>Note: A PreDestroy interceptor method must not throw application exceptions, but it
 * may be declared to throw checked exceptions including the java.lang.Exception if the
 * same interceptor method interposes on business or timeout methods in addition to
 * lifecycle events. If a PreDestroy interceptor method returns a value, it is ignored by
 * the container.</i></li>
 * <li>The method defined on a non-interceptor class must have the following signature:
 * <p>
 * void &#060;METHOD&#062;()</li>
 * <li>The method on which PreDestroy is applied may be public, protected, package private
 * or private.</li>
 * <li>The method must not be static.</li>
 * <li>The method should not be final.</li>
 * <li>If the method throws an unchecked exception it is ignored by the container.</li>
 * </ul>
 *
 * @see javax.annotation.PostConstruct
 * @see javax.annotation.Resource
 * @since 1.0.2
 * @author gaia
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreDestroy {

}
