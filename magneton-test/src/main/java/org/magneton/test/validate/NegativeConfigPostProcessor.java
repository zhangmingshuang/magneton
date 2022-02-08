package org.magneton.test.validate;

import java.lang.annotation.Annotation;

import javax.annotation.Nullable;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

/**
 * {@code @Negative} 验证注解的元素必须是负数
 *
 * <p>
 * {@code @NegativeOrZero} 验证注解的元素必须是负数或 0
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class NegativeConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setAllNumberMinValue(-128);
		config.setAllNumberMaxValue(-1);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Negative.class, NegativeOrZero.class };
	}

}
