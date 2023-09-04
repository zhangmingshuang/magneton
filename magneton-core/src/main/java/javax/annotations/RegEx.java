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

import javax.annotation.Syntax;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This qualifier is used to denote String values that should be a Regular expression.
 * <p>
 * When this annotation is applied to a method it applies to the method return value.
 */
@Documented
@Syntax("RegEx")
@TypeQualifierNickname
@Retention(RetentionPolicy.RUNTIME)
public @interface RegEx {

	When when() default When.ALWAYS;

	class Checker implements TypeQualifierValidator<RegEx> {

		public When forConstantValue(RegEx annotation, Object value) {
			if (!(value instanceof String)) {
				return When.NEVER;
			}
			try {
				Pattern.compile((String) value);
			}
			catch (PatternSyntaxException e) {
				return When.NEVER;
			}
			return When.ALWAYS;

		}

	}

}
