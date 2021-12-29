package org.magneton.test.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/8/17
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Definition {

	private Class clazz;

	@Nullable
	private Field field;

	@Nullable
	private List<Class> generics;

	@Nullable
	private Map<Class, Annotation> annotations;

	@Nullable
	private List<Definition> childDefinitions;

	public Definition resetClazz(Class<?> componentType) {
		this.clazz = componentType;
		return this;
	}

}
