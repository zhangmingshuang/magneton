package org.magneton.test.test.validate;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.Nullable;

import org.magneton.test.test.annotation.TestAutowired;
import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.parser.Definition;
import org.magneton.test.test.util.AnnotationUtil;
import org.hibernate.validator.constraints.Range;

/**
 * {@link Range}
 *
 * <p>
 * {@link javax.validation.constraints.Min}与{@link javax.validation.constraints.Max}的组合
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class RangeConfigPostProcessor extends AbstractConfigPostProcessor {

	@TestAutowired
	private MaxConfigPostProcessor maxConfigPostProcessor;

	@TestAutowired
	private MinConfigPostProcessor minConfigPostProcessor;

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
		long min = (long) metadata.get("min");
		long max = (long) metadata.get("max");

		this.minConfigPostProcessor.setByte(config, min);
		this.minConfigPostProcessor.setShort(config, min);
		this.minConfigPostProcessor.setInt(config, min);
		this.minConfigPostProcessor.setLong(config, min);
		this.minConfigPostProcessor.setBigDecimal(config, min);
		this.minConfigPostProcessor.setBigInteger(config, min);

		this.maxConfigPostProcessor.setByte(config, max);
		this.maxConfigPostProcessor.setShort(config, max);
		this.maxConfigPostProcessor.setInt(config, max);
		this.maxConfigPostProcessor.setLong(config, max);
		this.maxConfigPostProcessor.setBigDecimal(config, max);
		this.maxConfigPostProcessor.setBigInteger(config, max);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Range.class };
	}

}
