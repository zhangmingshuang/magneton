package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import java.lang.annotation.Annotation;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@TestComponent
@Slf4j
public class Jsr303PatternProcessor implements AnnotationProcessor {

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		String className = Inject.getRoot().getName();
		log.warn("框架不支持@Pattern的反向数据生成{}#{}", className, Optional.ofNullable(inject.getObject()).orElse(""));
	}

	@Override
	public boolean processable(Class annotationType) {
		return Pattern.class == annotationType;
	}

}
