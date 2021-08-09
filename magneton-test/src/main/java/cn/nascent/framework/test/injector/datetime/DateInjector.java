package cn.nascent.framework.test.injector.datetime;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;

/**
 * .
 *
 * @author zhangmsh 2021/8/4
 * @since
 */
@TestComponent
public class DateInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		if (java.sql.Date.class.isAssignableFrom(inject.getInectType())) {
			return new java.sql.Date(System.currentTimeMillis());
		}
		if (Date.class.isAssignableFrom(inject.getInectType())) {
			return new Date();
		}
		if (Calendar.class.isAssignableFrom(inject.getInectType())) {
			return Calendar.getInstance();
		}
		if (Instant.class.isAssignableFrom(inject.getInectType())) {
			return Instant.now();
		}
		if (LocalDate.class.isAssignableFrom(inject.getInectType())) {
			return LocalDate.now();
		}
		if (LocalDateTime.class.isAssignableFrom(inject.getInectType())) {
			return LocalDateTime.now();
		}
		if (LocalTime.class.isAssignableFrom(inject.getInectType())) {
			return LocalTime.now();
		}
		if (MonthDay.class.isAssignableFrom(inject.getInectType())) {
			return MonthDay.now();
		}
		if (OffsetDateTime.class.isAssignableFrom(inject.getInectType())) {
			return OffsetDateTime.now();
		}
		if (Year.class.isAssignableFrom(inject.getInectType())) {
			return Year.now();
		}
		if (YearMonth.class.isAssignableFrom(inject.getInectType())) {
			return YearMonth.now();
		}
		if (ZonedDateTime.class.isAssignableFrom(inject.getInectType())) {
			return ZonedDateTime.now();
		}
		if (HijrahDate.class.isAssignableFrom(inject.getInectType())) {
			return HijrahDate.now();
		}
		if (JapaneseDate.class.isAssignableFrom(inject.getInectType())) {
			return JapaneseDate.now();
		}
		if (MinguoDate.class.isAssignableFrom(inject.getInectType())) {
			return MinguoDate.now();
		}
		if (ThaiBuddhistDate.class.isAssignableFrom(inject.getInectType())) {
			return ThaiBuddhistDate.now();
		}
		return null;
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		return Array.newInstance(inject.getInectType().getComponentType(), length);
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { Date.class, Date[].class, java.sql.Date.class, java.sql.Date[].class, Calendar.class,
				Calendar[].class, Instant.class, Instant[].class, LocalDate.class, LocalDate[].class,
				LocalDateTime.class, LocalDateTime[].class, LocalTime.class, LocalTime[].class, MonthDay.class,
				MonthDay[].class, OffsetDateTime.class, OffsetDateTime[].class, Year.class, Year[].class,
				YearMonth.class, YearMonth[].class, ZonedDateTime.class, ZonedDateTime[].class, HijrahDate.class,
				HijrahDate[].class, JapaneseDate.class, JapaneseDate[].class, MinguoDate.class, MinguoDate[].class,
				ThaiBuddhistDate.class, ThaiBuddhistDate[].class };
	}

}
