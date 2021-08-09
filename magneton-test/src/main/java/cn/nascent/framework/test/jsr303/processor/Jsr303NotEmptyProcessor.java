package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.InjectorFactory;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import java.lang.annotation.Annotation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * {@code @NotEmpty}.
 *
 * <p>
 * 验证注解的元素值不为 null 且不为空（字符串长度不为 0、集合大小不为 0）
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
public class Jsr303NotEmptyProcessor implements AnnotationProcessor {

	@TestAutowired
	private InjectorFactory injectorFactory;

	@Override
	public boolean processable(Class annotationType) {
		return NotEmpty.class == annotationType || NotBlank.class == annotationType || NotNull.class == annotationType;
	}

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		if (injectType.isDemon()) {
			dataStatement.breakNext(null);
			return;
		}
		if (dataStatement.getValue() == null) {
			Object value = this.injectorFactory.injectRequired(config, Inject.of(inject.getInectType()), injectType);
			dataStatement.setValue(value);
		}
	}

}
