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

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

public interface TypeQualifierValidator<A extends Annotation> {

	/**
	 * Given a type qualifier, check to see if a known specific constant value is an
	 * instance of the set of values denoted by the qualifier.
	 * @param annotation the type qualifier
	 * @param value the value to check
	 * @return a value indicating whether or not the value is an member of the values
	 * denoted by the type qualifier
	 */
	@Nonnull
	When forConstantValue(@Nonnull A annotation, Object value);

}
