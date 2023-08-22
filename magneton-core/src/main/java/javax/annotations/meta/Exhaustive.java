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

package javax.annotations.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be applied to the value() element of an annotation that is
 * annotated as a TypeQualifier. This is only appropriate if the value field returns a
 * value that is an Enumeration.
 * <p>
 * Applications of the type qualifier with different values are exclusive, and the
 * enumeration is an exhaustive list of the possible values.
 * <p>
 * For example, the following defines a type qualifier such that if you know a value is
 * neither {@literal @Foo(Color.Red)} or {@literal @Foo(Color.Blue)}, then the value must
 * be {@literal @Foo(Color.Green)}. And if you know it is {@literal @Foo(Color.Green)},
 * you know it cannot be {@literal @Foo(Color.Red)} or {@literal @Foo(Color.Blue)}
 * <p>
 * <pre>
 * &#064;TypeQualifier  &#064;interface Foo {
 *     enum Color {RED, BLUE, GREEN};
 *     &#064;Exhaustive Color value();
 * }
 * </pre>
 * @author gaia
 * @since 1.0.2
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Exhaustive {

}
