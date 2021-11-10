package org.magneton.test.validate;

import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
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
import javax.validation.constraints.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.magneton.test.ChaosTest;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 * @see FutureConfigPostProcessor
 */
class FutureConfigProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	public static class TestA {

		@Future
		private Date date;

		@Future
		private Calendar calendar;

		@Future
		private Instant instant;

		@Future
		private LocalDate localDate;

		@Future
		private LocalDateTime localDateTime;

		@Future
		private LocalTime localTime;

		@Future
		private MonthDay monthDay;

		@Future
		protected OffsetDateTime offsetDateTime;

		@Future
		private Year year;

		@Future
		private YearMonth yearMonth;

		@Future
		private ZonedDateTime zonedDateTime;

		@Future
		private HijrahDate hijrahDate;

		@Future
		private JapaneseDate japaneseDate;

		@Future
		private MinguoDate minguoDate;

		@Future
		private ThaiBuddhistDate thaiBuddhistDate;

	}

	@RepeatedTest(10)
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

}
