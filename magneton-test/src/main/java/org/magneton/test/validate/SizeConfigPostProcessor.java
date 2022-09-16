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
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.constraints.Size;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;

/**
 * {@link Size} 字符串、集合、Map、及数组的 length/size.
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class SizeConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
		int min = (int) metadata.get("min");
		int max = (int) metadata.get("max");
		config.setMinSize(min).setMaxSize(max);

		// 对于字符串，其他的处理逻辑比当前的逻辑应该更高
		int minCharSequenceLength = config.getMinCharSequenceLength();
		if (minCharSequenceLength < min) {
			config.setMinCharSequenceLength(min);
		}
		int maxCharSequenceLength = config.getMaxCharSequenceLength();
		if (maxCharSequenceLength > max) {
			config.setMaxCharSequenceLength(max);
		}
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Size.class };
	}

}
