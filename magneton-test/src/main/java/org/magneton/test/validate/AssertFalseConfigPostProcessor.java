package org.magneton.test.validate;

import java.lang.annotation.Annotation;

import javax.annotation.Nullable;
import javax.validation.constraints.AssertFalse;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

/**
 * JSR303 {@link AssertFalse} 值必须为 {@code false}.
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class AssertFalseConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setBooleanTrueProbability(0);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { AssertFalse.class };
	}

}
