package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
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
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;

/**
 * .
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
public abstract class AbstractJsr303DateProcessor implements AnnotationProcessor {

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    Object value;
    if (java.sql.Date.class.isAssignableFrom(inject.getInectType())) {
      value = new java.sql.Date(System.currentTimeMillis() + this.getPlusHourMills(injectType));
    } else if (Date.class.isAssignableFrom(inject.getInectType())) {
      value = new Date(System.currentTimeMillis() + this.getPlusHourMills(injectType));
    } else if (Calendar.class.isAssignableFrom(inject.getInectType())) {
      Calendar instance = Calendar.getInstance();
      instance.add(Calendar.MILLISECOND, this.getPlusHourMills(injectType));
      value = instance;
    } else if (Instant.class.isAssignableFrom(inject.getInectType())) {
      value = Instant.now().plusMillis(this.getPlusHourMills(injectType));
    } else if (LocalDate.class.isAssignableFrom(inject.getInectType())) {
      value = LocalDate.now().plusDays(this.getPlusDay(injectType));
    } else if (LocalDateTime.class.isAssignableFrom(inject.getInectType())) {
      value = LocalDateTime.now().plus(this.getPlusHourMills(injectType), ChronoUnit.MILLIS);
    } else if (LocalTime.class.isAssignableFrom(inject.getInectType())) {
      value = LocalTime.now().plus(this.getPlusHourMills(injectType), ChronoUnit.MILLIS);
    } else if (MonthDay.class.isAssignableFrom(inject.getInectType())) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_MONTH, this.getPlusDay(injectType));
      value = MonthDay.of(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    } else if (OffsetDateTime.class.isAssignableFrom(inject.getInectType())) {
      value = OffsetDateTime.now().plus(this.getPlusHourMills(injectType), ChronoUnit.MILLIS);
    } else if (Year.class.isAssignableFrom(inject.getInectType())) {
      value = Year.now().plusYears(this.getPlusYear(injectType));
    } else if (YearMonth.class.isAssignableFrom(inject.getInectType())) {
      value = YearMonth.now().plusMonths(this.getPlusMonth(injectType));
    } else if (ZonedDateTime.class.isAssignableFrom(inject.getInectType())) {
      value = ZonedDateTime.now().plus(this.getPlusHourMills(injectType), ChronoUnit.MILLIS);
    } else if (HijrahDate.class.isAssignableFrom(inject.getInectType())) {
      value = HijrahDate.now().plus(this.getPlusDay(injectType), ChronoUnit.DAYS);
    } else if (JapaneseDate.class.isAssignableFrom(inject.getInectType())) {
      value = JapaneseDate.now().plus(this.getPlusDay(injectType), ChronoUnit.DAYS);
    } else if (MinguoDate.class.isAssignableFrom(inject.getInectType())) {
      value = MinguoDate.now().plus(this.getPlusDay(injectType), ChronoUnit.DAYS);
    } else if (ThaiBuddhistDate.class.isAssignableFrom(inject.getInectType())) {
      value = ThaiBuddhistDate.now().plus(this.getPlusDay(injectType), ChronoUnit.DAYS);
    } else {
      throw new UnsupportedOperationException("不支持的时间类型" + inject.getInectType());
    }
    dataStatement.setValue(value);
  }

  protected int getPlusMonth(InjectType injectType) {
    int month = this.getMonth();
    return this.reverseIfNeed(injectType, month);
  }

  protected abstract int getMonth();

  protected int getPlusYear(InjectType injectType) {
    int year = this.getYear();
    return this.reverseIfNeed(injectType, year);
  }

  protected abstract int getYear();

  protected int getPlusDay(InjectType injectType) {
    int day = this.getDay();
    return this.reverseIfNeed(injectType, day);
  }

  protected abstract int getDay();

  protected int getPlusHourMills(InjectType injectType) {
    int hourMills = this.getHourMills();
    return this.reverseIfNeed(injectType, hourMills);
  }

  protected abstract int getHourMills();

  protected int reverseIfNeed(InjectType injectType, int value) {
    return injectType.isDemon() ? -1 * value : value;
  }
}
