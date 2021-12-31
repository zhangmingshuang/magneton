package org.magneton.test.test.validate;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.Nullable;

import org.magneton.test.test.config.Config;
import org.magneton.test.test.config.ConfigPostProcessor;
import org.magneton.test.test.core.TraceChain;
import org.magneton.test.test.exception.ConfigPostProcessException;
import org.magneton.test.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
public abstract class AbstractConfigPostProcessor implements ConfigPostProcessor {

	@Override
	@SuppressWarnings("java:S1181")
	public void beforeConfig(Config config, Definition definition) {
		Map<Class<?>, Annotation> annotations = definition.getAnnotations();
		if (annotations == null || annotations.isEmpty()) {
			return;
		}
		Class[] jsrAnnotations = this.jsrAnnotations();
		if (jsrAnnotations == null || jsrAnnotations.length < 1) {
			return;
		}
		try {
			for (Class<?> annotationClass : jsrAnnotations) {
				Annotation annotation = annotations.get(annotationClass);
				if (annotation != null) {
					this.doPostProcessor(annotation, config, definition);
				}
			}
		}
		catch (Throwable e) {
			throw new ConfigPostProcessException(TraceChain.current().toString(), e);
		}
	}

	protected abstract void doPostProcessor(Annotation annotation, Config config, Definition definition);

	@Nullable
	protected abstract Class[] jsrAnnotations();

}
