package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import cn.nascent.framework.test.util.NumberUtil;
import java.lang.annotation.Annotation;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * {@code @Positive} 验证注解的元素必须是正数
 *
 * <p>
 * {@code @PositiveOrZero} 验证注解的元素必须是正数或 0
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@TestComponent
public class Jsr3030PositiveProcessor implements AnnotationProcessor {

	@Override
	public boolean processable(Class annotationType) {
		return Positive.class == annotationType || PositiveOrZero.class == annotationType;
	}

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		if (injectType.isDemon()) {
			dataStatement.breakNext(NumberUtil.cast(inject.getInectType(), -1));
			return;
		}
		dataStatement.setValue(NumberUtil.cast(inject.getInectType(), 1));
	}

}
