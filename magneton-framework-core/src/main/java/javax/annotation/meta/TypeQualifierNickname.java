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

package javax.annotation.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation is applied to a annotation, and marks the annotation as being a
 * qualifier nickname. Applying a nickname annotation X to a element Y should be
 * interpreted as having the same meaning as applying all of annotations of X (other than
 * QualifierNickname) to Y.
 * <p>
 * Thus, you might define a qualifier SocialSecurityNumber as follows:
 * </p>
 * <pre>{@code
 *      &#064;Documented
 *      &#064;TypeQualifierNickname
 *      &#064;Pattern("[0-9]{3}-[0-9]{2}-[0-9]{4}")
 *      &#064;Retention(RetentionPolicy.RUNTIME)
 *      public interface SocialSecurityNumber {
 *      }
 * }</pre>
 * @author gaia
 * @since 1.0.2
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
public @interface TypeQualifierNickname {

}
