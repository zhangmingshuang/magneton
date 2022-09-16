/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.validate;

import java.lang.annotation.Annotation;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.model.StringModel;
import org.magneton.test.parser.Definition;

/**
 * {@code @Email(regexp=正则表达式,flag=标志的模式)}
 *
 * <p>
 * 验证注解的元素值是 {@code Email}，也可以通过 regexp 和 flag 指定自定义的 email 格式.
 *
 * <p>
 * 该处理器只会生成一个标准的Email数据，如果是特殊的Email数据，需要自己定义处理器
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class EmailConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setStringMode(StringModel.EMAIL);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Email.class, org.hibernate.validator.constraints.Email.class };
	}

}
