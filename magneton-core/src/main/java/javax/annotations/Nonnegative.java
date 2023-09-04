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

import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to annotate a value that should only contain nonnegative
 * values.
 * <p>
 * When this annotation is applied to a method it applies to the method return value.
 * @author gaia
 * @since 1.0.2
 */
@Documented
@TypeQualifier(applicableTo = Number.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Nonnegative {

	When when() default When.ALWAYS;

	class Checker implements TypeQualifierValidator<Nonnegative> {

		public When forConstantValue(Nonnegative annotation, Object v) {
			if (!(v instanceof Number)) {
				return When.NEVER;
			}
			boolean isNegative;
			Number value = (Number) v;
			if (value instanceof Long) {
				isNegative = value.longValue() < 0;
			}
			else if (value instanceof Double) {
				isNegative = value.doubleValue() < 0;
			}
			else if (value instanceof Float) {
				isNegative = value.floatValue() < 0;
			}
			else {
				isNegative = value.intValue() < 0;
			}

			if (isNegative) {
				return When.NEVER;
			}
			else {
				return When.ALWAYS;
			}
		}

	}

}
