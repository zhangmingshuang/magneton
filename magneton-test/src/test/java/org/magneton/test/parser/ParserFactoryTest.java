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

package org.magneton.test.parser;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.magneton.test.helper.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
class ParserFactoryTest {

	private final ParserFactory parserFactory = new ParserFactory();

	@Test
	void parseLong() {
		Definition definition = this.parserFactory.parse(Long.class);
		Assertions.assertEquals("java.lang.Long", definition.getClazz().getName());
		Assertions.assertNull(definition.getChildDefinitions());
		Assertions.assertNull(definition.getGenerics());
		Assertions.assertEquals(0, definition.getAnnotations().size());
		System.out.println(definition);
	}

	@Test
	void parseA() {
		Definition definition = this.parserFactory.parse(A.class);
		Assertions.assertEquals(2, definition.getChildDefinitions().size());
		for (Definition childDefinition : definition.getChildDefinitions()) {
			Assertions.assertEquals(1, childDefinition.getAnnotations().size());
		}
		System.out.println(definition);
	}

	@Test
	void testB() {
		Definition definition = this.parserFactory.parse(B.class);
		Assertions.assertEquals(3, definition.getChildDefinitions().size());
		for (Definition childDefinition : definition.getChildDefinitions()) {
			String simpleName = childDefinition.getClazz().getSimpleName();
			if ("A".equalsIgnoreCase(simpleName)) {
				Assertions.assertEquals(2, childDefinition.getChildDefinitions().size());
			}
			else if ("B".equalsIgnoreCase(simpleName)) {
				Assertions.assertEquals(3, childDefinition.getChildDefinitions().size());
			}
		}
		System.out.println(definition);
	}

	@Test
	void testC() {
		Definition definition = this.parserFactory.parse(C.class);
		System.out.println(definition);
	}

	@Test
	void testD() {
		Definition parse = this.parserFactory.parse(D.class);
		Assertions.assertEquals(1, parse.getChildDefinitions().size());
		Definition definition = parse.getChildDefinitions().get(0);
		Map<Class<?>, Annotation> annotations = definition.getAnnotations();
		Human.sout(annotations);
		Assertions.assertNotNull(annotations.get(AssertTrue.class));
	}

	public static class A {

		@Size(min = 1, max = 12)
		private long a;

		@NotNull
		private String b;

	}

	public static class B {

		private A a;

		private String str;

		private B b;

	}

	public static class C {

		private List<String> list;

	}

	public static class D {

		@AssertTrue
		private Boolean bool;

	}

}
