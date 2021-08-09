package cn.nascent.framework.test.jsr303.processor;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.annotation.TestSort;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.AnnotationProcessor;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import cn.nascent.framework.test.util.AnnotationUtils;
import cn.nascent.framework.test.util.NumberUtil;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Digits;

/**
 * {@code @Digits(integer,fraction) }整数部分位数不超过integer,小数部分位数不超过fraction.
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
@TestSort(TestSort.LOWEST_PRECEDENCE - 1)
public class Jsr303DigitsProcessor implements AnnotationProcessor {

	@Override
	public boolean processable(Class annotationType) {
		return Digits.class == annotationType;
	}

	@Override
	public void process(Config config, InjectType injectType, Inject inject, Annotation annotation,
			DataStatement dataStatement) {
		Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
		// 整数部分
		int integer = (int) metadata.get("integer");
		// 小数部分
		int fraction = (int) metadata.get("fraction");
		if (injectType.isDemon()) {
			// 不能超过，相反就是超过
			integer++;
			dataStatement.breakNext(NumberUtil.cast(inject.getInectType(), integer * 10));
			return;
		}
		int legitimateInteger = ThreadLocalRandom.current().nextInt(integer * 10 - 1);
		int legitimateFraction = ThreadLocalRandom.current().nextInt(fraction * 10 - 1);
		dataStatement.setValue(NumberUtil.cast(inject.getInectType(), legitimateInteger + "." + legitimateFraction));
	}

}
