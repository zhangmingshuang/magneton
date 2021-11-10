package org.magneton.test.parser;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.helper.Human;

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
		Assertions.assertEquals(1, definition.getAnnotations().size());
		System.out.println(definition);
	}

	public static class A {

		@Size(min = 1, max = 12)
		private long a;

		@NotNull
		private String b;

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

	public static class B {

		private A a;

		private String str;

		private B b;

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

	public static class C {

		private List<String> list;

	}

	@Test
	void testC() {
		Definition definition = this.parserFactory.parse(C.class);
		System.out.println(definition);
	}

	public static class D {

		@AssertTrue
		private Boolean bool;

	}

	@Test
	void testD() {
		Definition parse = this.parserFactory.parse(D.class);
		Assertions.assertEquals(1, parse.getChildDefinitions().size());
		Definition definition = parse.getChildDefinitions().get(0);
		Map<Class, Annotation> annotations = definition.getAnnotations();
		Human.sout(annotations);
		Assertions.assertNotNull(annotations.get(AssertTrue.class));
	}

}
