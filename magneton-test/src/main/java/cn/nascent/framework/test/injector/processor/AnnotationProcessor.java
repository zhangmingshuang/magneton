package cn.nascent.framework.test.injector.processor;

import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import java.lang.annotation.Annotation;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public interface AnnotationProcessor {

	void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement);

	boolean processable(Class annotationType);

}
