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

package javax.annotation;

import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation a value that is of a particular syntax, such as Java syntax or regular
 * expression syntax. This can be used to provide syntax checking of constant values at
 * compile time, run time checking at runtime, and can assist IDEs in deciding how to
 * interpret String constants (e.g., should a refactoring that renames method {@code x()}
 * to {@code y()} update the String constant {@code "x()"}).
 */
@Documented
@TypeQualifier(applicableTo = CharSequence.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Syntax {

	/**
	 * Value indicating the particular syntax denoted by this annotation. Different tools
	 * will recognize different syntaxes, but some proposed canonical values are:
	 * <ul>
	 * <li>"Java"
	 * <li>"RegEx"
	 * <li>"JavaScript"
	 * <li>"Ruby"
	 * <li>"Groovy"
	 * <li>"SQL"
	 * <li>"FormatString"
	 * </ul>
	 * <p>
	 * Syntax names can be followed by a colon and a list of key value pairs, separated by
	 * commas. For example, "SQL:dialect=Oracle,version=2.3". Tools should ignore any keys
	 * they don't recognize.
	 * @return a name indicating the particular syntax.
	 */
	String value();

	When when() default When.ALWAYS;

}
