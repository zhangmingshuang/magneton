package org.magneton.test.injector.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.exception.UnsupportedTypeCreateException;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
public class DateInjector extends AbstractInjector {

  @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
  @Override
  protected Object createValue(Definition definition, Config config, InjectType injectType) {
    Date date = ConfigProcessorFactory.of(injectType).nextDate(config, definition);
    if (date == null) {
      return null;
    }
    long dateTime = date.getTime();

    Class clazz = definition.getClazz();
    if (java.sql.Date.class.isAssignableFrom(clazz)) {
      return new java.sql.Date(dateTime);
    }
    if (Date.class.isAssignableFrom(clazz)) {
      return date;
    }
    if (Calendar.class.isAssignableFrom(clazz)) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(date);
      return instance;
    }
    if (Instant.class.isAssignableFrom(clazz)) {
      return date.toInstant();
    }
    if (LocalDate.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
      return instant.atZone(zoneId).toLocalDate();
    }
    if (LocalDateTime.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      return instant.atZone(zoneId).toLocalDateTime();
    }
    if (LocalTime.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      return instant.atZone(zoneId).toLocalTime();
    }
    if (MonthDay.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDateTime now = instant.atZone(zoneId).toLocalDateTime();
      return MonthDay.of(now.getMonth(), now.getDayOfMonth());
    }
    if (OffsetDateTime.class.isAssignableFrom(clazz)) {
      return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    if (Year.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDateTime now = instant.atZone(zoneId).toLocalDateTime();
      return Year.of(now.getYear());
    }
    if (YearMonth.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDateTime now = instant.atZone(zoneId).toLocalDateTime();
      return YearMonth.of(now.getYear(), now.getMonth());
    }
    if (ZonedDateTime.class.isAssignableFrom(clazz)) {
      return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    if (HijrahDate.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDate localDate = instant.atZone(zoneId).toLocalDate();
      return HijrahDate.from(localDate);
    }
    if (JapaneseDate.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDate localDate = instant.atZone(zoneId).toLocalDate();
      return JapaneseDate.from(localDate);
    }
    if (MinguoDate.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDate localDate = instant.atZone(zoneId).toLocalDate();
      return MinguoDate.from(localDate);
    }
    if (ThaiBuddhistDate.class.isAssignableFrom(clazz)) {
      Instant instant = date.toInstant();
      ZoneId zoneId = ZoneId.systemDefault();
      LocalDate localDate = instant.atZone(zoneId).toLocalDate();
      return ThaiBuddhistDate.from(localDate);
    }
    throw new UnsupportedTypeCreateException("时间类型%s不支持", clazz);
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {
      Date.class,
      Date[].class,
      java.sql.Date.class,
      java.sql.Date[].class,
      Calendar.class,
      Calendar[].class,
      Instant.class,
      Instant[].class,
      LocalDate.class,
      LocalDate[].class,
      LocalDateTime.class,
      LocalDateTime[].class,
      LocalTime.class,
      LocalTime[].class,
      MonthDay.class,
      MonthDay[].class,
      OffsetDateTime.class,
      OffsetDateTime[].class,
      Year.class,
      Year[].class,
      YearMonth.class,
      YearMonth[].class,
      ZonedDateTime.class,
      ZonedDateTime[].class,
      HijrahDate.class,
      HijrahDate[].class,
      JapaneseDate.class,
      JapaneseDate[].class,
      MinguoDate.class,
      MinguoDate[].class,
      ThaiBuddhistDate.class,
      ThaiBuddhistDate[].class
    };
  }
}
