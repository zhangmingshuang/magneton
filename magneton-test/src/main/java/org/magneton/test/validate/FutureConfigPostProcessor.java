package org.magneton.test.validate;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

import java.lang.annotation.Annotation;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import javax.annotation.Nullable;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

/**
 * {@code @FutureOrPresent} 验证注解的元素值（日期类型）比当前时间晚，或者是当前时间.
 *
 * <p>
 * {@code @Future} 验证注解的元素值（日期类型）比当前时间晚
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 */
@TestComponent
public class FutureConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		Long minDateMillis = config.getMinDateMillis();
		Long maxDateMillis = config.getMaxDateMillis();

		Class clazz = definition.getClazz();
		if (YearMonth.class.isAssignableFrom(clazz)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			minDateMillis = Math.max(minDateMillis, calendar.getTimeInMillis());
		}
		else if (LocalTime.class.isAssignableFrom(clazz)) {
			minDateMillis = Math.max(minDateMillis, System.currentTimeMillis() + 10 * 1000L);
			maxDateMillis = minDateMillis;
		}
		else if (Year.class.isAssignableFrom(clazz)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, 1);
			minDateMillis = Math.max(minDateMillis, calendar.getTimeInMillis());
		}
		else {
			if (minDateMillis == null || minDateMillis < System.currentTimeMillis()) {
				minDateMillis = System.currentTimeMillis() + 86400 * 1000L;
			}
		}

		if (maxDateMillis < minDateMillis) {
			maxDateMillis = minDateMillis + 86400 * 1000L;
		}
		config.setMinDateMillis(minDateMillis).setMaxDateMillis(maxDateMillis);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Future.class, FutureOrPresent.class };
	}

}
