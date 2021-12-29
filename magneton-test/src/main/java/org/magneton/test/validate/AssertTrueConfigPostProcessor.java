package org.magneton.test.validate;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.AssertTrue;

/**
 * JSR303 {@link AssertTrue} 值必须为 {@code true}.
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class AssertTrueConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setBooleanTrueProbability(100);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { AssertTrue.class };
	}

}
