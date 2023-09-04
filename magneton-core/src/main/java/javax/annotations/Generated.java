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
 * 生成的代码的标记注解
 * <p>
 * 还可用于在单个文件中区分用户编写的代码和生成的代码
 *
 * @since 1.0.2
 * @author gaia
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.PACKAGE, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD,
		ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER })
public @interface Generated {

	/**
	 * 代码生成器的名称。 推荐的约定是使用代码生成器的完全限定名称。
	 * <p>
	 * 例如：<code>com.acme.generator.CodeGen</code>。
	 * @return 代码生成器的名称
	 */
	String[] value();

	/**
	 * 代码生成的日期
	 * @return 代码生成的日期，默认为空
	 */
	String date() default "";

	/**
	 * 注释说明
	 * @return 注释说明，默认为空
	 */
	String comments() default "";

}
