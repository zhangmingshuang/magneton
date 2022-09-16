/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

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

import javax.validation.constraints.Future;

import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

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

	@RepeatedTest(10)
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

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

}
