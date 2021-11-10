package org.magneton.test.validate;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;

/**
 * {@code @NotEmpty}验证注解的元素值不为 null 且不为空（字符串长度不为 0、集合大小不为 0）
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class NotEmptyConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		int minSize = config.getMinSize();
		if (minSize < 1) {
			config.setMinSize(1);
		}
		int maxSize = config.getMaxSize();
		if (maxSize < 1) {
			config.setMaxSize(1);
		}
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { NotEmpty.class, org.hibernate.validator.constraints.NotEmpty.class };
	}

}
