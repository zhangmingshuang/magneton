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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The field or method to which this annotation is applied can only be accessed when
 * holding a particular lock, which may be a built-in (synchronization) lock, or may be an
 * explicit {@link java.util.concurrent.locks.Lock}.
 * <p>
 * The argument determines which lock guards the annotated field or method:
 * <ul>
 * <li>this : The string literal "this" means that this field is guarded by the class in
 * which it is defined.
 * <li>class-name.this : For inner classes, it may be necessary to disambiguate 'this';
 * the class-name.this designation allows you to specify which 'this' reference is
 * intended
 * <li>itself : For reference fields only; the object to which the field refers.
 * <li>field-name : The lock object is referenced by the (instance or static) field
 * specified by field-name.
 * <li>class-name.field-name : The lock object is reference by the static field specified
 * by class-name.field-name.
 * <li>method-name() : The lock object is returned by calling the named nil-ary method.
 * <li>class-name.class : The Class object for the specified class should be used as the
 * lock object.
 * </ul>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {

	String value();

}
