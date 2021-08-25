package org.magneton.test.injector.datetime;

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
import java.util.List;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 * @see DateInjector
 */
class DateInjectorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  @ToString
  public static class TestA {
    private Date date;
    private List<Date> dates;
  }

  @Test
  void testA() {
    TestA test = ChaosTest.create(TestA.class, this.config, this.angle);
    System.out.println(test);
    Assertions.assertNotNull(test.date);
    Assertions.assertNotNull(test.dates);
  }

  @ToString
  public static class TestB {
    private Date date;
  }

  @Test
  void testB() {
    Config copied = Config.copyOf(this.config);
    TestB test = ChaosTest.create(TestB.class, copied, this.angle);
    Human.sout(test);
    Calendar calendar = Calendar.getInstance();
    Assertions.assertEquals(calendar.get(Calendar.MINUTE), test.date.getMinutes());
    Assertions.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), test.date.getHours());
    Assertions.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), test.date.getDate());
    Assertions.assertEquals(calendar.get(Calendar.MONTH), test.date.getMonth());
    Assertions.assertEquals(calendar.get(Calendar.YEAR), test.date.getYear() + 1900);
  }

  @ToString
  public static class TestC {
    private Date date;
    private Calendar calendar;
    private Instant instant;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private LocalTime localTime;
    private MonthDay monthDay;
    protected OffsetDateTime offsetDateTime;
    private Year year;
    private YearMonth yearMonth;
    private ZonedDateTime zonedDateTime;
    private HijrahDate hijrahDate;
    private JapaneseDate japaneseDate;
    private MinguoDate minguoDate;
    private ThaiBuddhistDate thaiBuddhistDate;
  }

  @Test
  void testC() {
    Config copied = Config.copyOf(this.config);
    TestC test = ChaosTest.create(TestC.class, copied, this.angle);
    Human.sout(test);
    Assertions.assertNotNull(test.date);
    Assertions.assertNotNull(test.calendar);
    Assertions.assertNotNull(test.instant);
    Assertions.assertNotNull(test.localDate);
    Assertions.assertNotNull(test.localDateTime);
    Assertions.assertNotNull(test.localTime);
    Assertions.assertNotNull(test.monthDay);
    Assertions.assertNotNull(test.offsetDateTime);
    Assertions.assertNotNull(test.year);
    Assertions.assertNotNull(test.yearMonth);
    Assertions.assertNotNull(test.zonedDateTime);
    Assertions.assertNotNull(test.hijrahDate);
    Assertions.assertNotNull(test.japaneseDate);
    Assertions.assertNotNull(test.minguoDate);
    Assertions.assertNotNull(test.thaiBuddhistDate);
  }
}
