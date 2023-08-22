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
package javax.annotations.concurrent;

import javax.annotation.concurrent.ThreadSafe;
import java.lang.annotation.*;

/**
 * The class to which this annotation is applied is not thread-safe. This annotation
 * primarily exists for clarifying the non-thread-safety of a class that might otherwise
 * be assumed to be thread-safe, despite the fact that it is a bad idea to assume a class
 * is thread-safe without good reason.
 *
 * @see ThreadSafe
 * @since 1.0.2
 * @author gaia
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface NotThreadSafe {

}
