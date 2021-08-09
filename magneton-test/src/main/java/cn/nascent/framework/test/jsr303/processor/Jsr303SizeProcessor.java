package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.InjectorFactory;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import cn.nascent.framework.test.util.AnnotationUtils;
import java.lang.annotation.Annotation;
import java.util.Map;
import javax.validation.constraints.Size;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
@TestComponent
public class Jsr303SizeProcessor implements AnnotationProcessor {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		if (injectType.isDemon()) {
			dataStatement.breakNext(null);
			return;
		}
		Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
		int min = (int) metadata.get("min");
		int max = (int) metadata.get("max");
		config.setMinSize(min);
		config.setMaxSize(max);
		dataStatement.setValue(this.injectorFactory.inject(config, Inject.of(inject.getInectType()), injectType));
	}

	@Override
	public boolean processable(Class annotationType) {
		return Size.class == annotationType;
	}

}
