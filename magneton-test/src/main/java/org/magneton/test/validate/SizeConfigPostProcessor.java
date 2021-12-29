package org.magneton.test.validate;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;
import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.Size;

/**
 * {@link Size} 字符串、集合、Map、及数组的 length/size.
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class SizeConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
		int min = (int) metadata.get("min");
		int max = (int) metadata.get("max");
		config.setMinSize(min).setMaxSize(max);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Size.class };
	}

}
