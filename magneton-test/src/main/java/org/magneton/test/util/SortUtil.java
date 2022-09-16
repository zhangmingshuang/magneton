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

package org.magneton.test.util;

import org.magneton.test.annotation.TestSort;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public class SortUtil {

	private SortUtil() {
	}

	@SuppressWarnings("SubtractionInCompareTo")
	@CanIgnoreReturnValue
	public static <T> List<T> sort(List<T> list) {
		list.sort((o1, o2) -> {
			TestSort s1 = AnnotationUtil.findAnnotations(o1.getClass(), TestSort.class);
			TestSort s2 = AnnotationUtil.findAnnotations(o2.getClass(), TestSort.class);
			return (s1 == null ? 0 : s1.value()) - (s2 == null ? 0 : s2.value());
		});
		return list;
	}

}
