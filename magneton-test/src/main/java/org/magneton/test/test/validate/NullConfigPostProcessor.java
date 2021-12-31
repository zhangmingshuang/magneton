package org.magneton.test.test.validate;

import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.parser.Definition;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;

/**
 * {@link Null} 数据一定是 {@code null}
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class NullConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setMinSize(-1).setMaxSize(-1).setNullableProbability(100);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Null.class };
	}

}
