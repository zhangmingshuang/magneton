package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import java.lang.annotation.Annotation;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

/**
 * {@code @AssertTrue} 与 {@code @AssertFalse}.
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
public class Jsr303AssertFalseAndTrueProcessor implements AnnotationProcessor {

	@Override
	public boolean processable(Class annotationType) {
		return AssertFalse.class == annotationType || AssertTrue.class == annotationType;
	}

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		if (annotation.annotationType() == AssertFalse.class) {
			this.doAssertFalse(config, injectType, inject, dataStatement);
		}
		else {
			this.doAssertTrue(config, injectType, inject, dataStatement);
		}
	}

	private void doAssertTrue(Config config, InjectType injectType, Inject inject, DataStatement dataStatement) {
		dataStatement.setValue(!injectType.isDemon());
	}

	private void doAssertFalse(Config config, InjectType injectType, Inject inject, DataStatement dataStatement) {
		dataStatement.setValue(injectType.isDemon());
	}

}
