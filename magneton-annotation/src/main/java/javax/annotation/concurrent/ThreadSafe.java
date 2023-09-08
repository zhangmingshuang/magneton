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
package javax.annotation.concurrent;

import java.lang.annotation.*;

/**
 * The class to which this annotation is applied is thread-safe. This means that no
 * sequences of accesses (reads and writes to public fields, calls to public methods) may
 * put the object into an invalid state, regardless of the interleaving of those actions
 * by the runtime, and without requiring any additional synchronization or coordination on
 * the part of the caller.
 *
 * @see NotThreadSafe
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {

}
