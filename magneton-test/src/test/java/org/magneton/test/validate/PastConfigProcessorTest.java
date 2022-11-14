package org.magneton.test.validate;

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

import javax.validation.constraints.Past;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 * @see PastConfigPostProcessor
 */
class PastConfigProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@RepeatedTest(100)
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

	public static class TestA {

		@Past
		private Date date;

		@Past
		private Calendar calendar;

		@Past
		private Instant instant;

		@Past
		private LocalDate localDate;

		@Past
		private LocalDateTime localDateTime;

		@Past
		private LocalTime localTime;

		@Past
		private MonthDay monthDay;

		@Past
		protected OffsetDateTime offsetDateTime;

		@Past
		private Year year;

		@Past
		private YearMonth yearMonth;

		@Past
		private ZonedDateTime zonedDateTime;

		@Past
		private HijrahDate hijrahDate;

		@Past
		private JapaneseDate japaneseDate;

		@Past
		private MinguoDate minguoDate;

		@Past
		private ThaiBuddhistDate thaiBuddhistDate;

	}

}
