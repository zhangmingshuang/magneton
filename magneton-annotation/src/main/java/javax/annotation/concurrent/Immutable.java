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
 * The class to which this annotation is applied is immutable. This means that its state
 * cannot be seen to change by callers. Of necessity this means that all public fields are
 * final, and that all public final reference fields refer to other immutable objects, and
 * that methods do not publish references to any internal state which is mutable by
 * implementation even if not by design. Immutable objects may still have internal mutable
 * state for purposes of performance optimization; some state variables may be lazily
 * computed, so long as they are computed from immutable state and that callers cannot
 * tell the difference.
 * <p>
 * Immutable objects are inherently thread-safe; they may be passed between threads or
 * published without synchronization.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Immutable {

}
