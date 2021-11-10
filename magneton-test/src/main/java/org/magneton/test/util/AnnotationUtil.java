package org.magneton.test.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.Constraint;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@Slf4j
public class AnnotationUtil {

	private AnnotationUtil() {
	}

	@Nullable
	public static <A extends Annotation> A findAnnotations(@Nullable Class clazz, Class<A> annotationClass) {
		if (clazz == null || clazz == Object.class) {
			return null;
		}
		Preconditions.checkNotNull(annotationClass, "annotationClass must be not null");
		Annotation annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null) {
			annotation = findAnnotations(clazz.getSuperclass(), annotationClass);
		}
		if (annotation == null) {
			Class[] interfaces = clazz.getInterfaces();
			for (Class interfaceClass : interfaces) {
				annotation = findAnnotations(interfaceClass, annotationClass);
				if (annotation != null) {
					return (A) annotation;
				}
			}
		}
		if (annotation == null) {
			Map<Class, Annotation> foundAnnotations = findAnnotations(clazz);
			return (A) foundAnnotations.get(annotationClass);
		}
		return (A) annotation;
	}

	@Nullable
	public static <A extends Annotation> A findAnnotations(Field field, Class<A> annotationClass) {
		Preconditions.checkNotNull(annotationClass, "annotationClass must be not null");
		Preconditions.checkNotNull(field, "Field class must be not null");
		return field.getAnnotation(annotationClass);
	}

	public static Map<Class, Annotation> findAnnotations(@Nullable Object object) {
		if (object == null) {
			return Collections.emptyMap();
		}
		if (Field.class.isAssignableFrom(object.getClass())) {
			Annotation[] fieldAnnotations = ((Field) object).getAnnotations();
			if (fieldAnnotations.length < 1) {
				return Collections.emptyMap();
			}
			Map<Class, Annotation> annotations = Maps.newHashMap();
			for (Annotation fieldAnnotation : fieldAnnotations) {
				annotations.put(fieldAnnotation.annotationType(), fieldAnnotation);
				Map<Class, Annotation> anns = findAnnotations(fieldAnnotation.annotationType());
				annotations.putAll(anns);
			}
			return annotations;
		}
		return findAnnotations(object.getClass());
	}

	public static Map<Class, Annotation> findAnnotations(@Nullable Class clazz) {
		if (clazz == null || clazz == Object.class) {
			return Collections.emptyMap();
		}
		Map<Class, Annotation> annotations = Maps.newHashMap();
		return findAnnotations(annotations, clazz);
	}

	private static Map<Class, Annotation> findAnnotations(Map<Class, Annotation> annotations, @Nullable Class clazz) {
		Preconditions.checkNotNull(annotations);
		if (isIgnore(clazz)) {
			return annotations;
		}
		Annotation[] clazzAnnotations = clazz.getAnnotations();
		for (Annotation clazzAnnotation : clazzAnnotations) {
			if (isIgnore(clazzAnnotation.annotationType())) {
				continue;
			}
			annotations.put(clazzAnnotation.annotationType(), clazzAnnotation);
			findAnnotations(annotations, clazzAnnotation.annotationType());
		}
		findAnnotations(annotations, clazz.getSuperclass());
		for (Class interfaceClazz : clazz.getInterfaces()) {
			findAnnotations(annotations, interfaceClazz);
		}
		return annotations;
	}

	@SuppressWarnings("OverlyComplexBooleanExpression")
	private static boolean isIgnore(Class clazz) {
		return clazz == null || clazz == Object.class || clazz == Documented.class || clazz == Retention.class
				|| clazz == Target.class || clazz == Repeatable.class || clazz == Constraint.class;
	}

	public static Map<String, Object> getMetadata(@Nullable Annotation annotation) {
		if (annotation == null) {
			return Collections.emptyMap();
		}
		Method[] methods = annotation.getClass().getMethods();
		if (methods.length < 1) {
			return Collections.emptyMap();
		}
		Map<String, Object> metadata = Maps.newHashMap();
		for (Method method : methods) {
			if (method.getDeclaringClass() == Object.class) {
				continue;
			}
			if (method.getParameters().length > 0) {
				continue;
			}
			try {
				metadata.put(method.getName(), method.invoke(annotation));
			}
			catch (Throwable e) {
				log.error("error on method {} in {}", method, annotation);
				throw new RuntimeException(e);
			}
		}
		return metadata;
	}

}
