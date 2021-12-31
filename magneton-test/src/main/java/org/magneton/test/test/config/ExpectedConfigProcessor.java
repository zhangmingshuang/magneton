package org.magneton.test.test.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.model.StringModel;
import org.magneton.test.test.model.generate.base.AbstractGenericGenerator;
import org.magneton.test.test.parser.Definition;
import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 预期数据处理.
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@ConfigWith(InjectType.EXPECTED)
public class ExpectedConfigProcessor implements ConfigProcessor {

	@Override
	public int nextSize(Config config, Definition definition) {
		int minSize = config.getMinSize();
		int maxSize = config.getMaxSize();
		if (minSize < 0 || maxSize < 0) {
			return -1;
		}
		if (minSize < 1 || maxSize < 1) {
			return 0;
		}
		if (minSize == maxSize) {
			return minSize;
		}
		if (minSize > maxSize) {
			minSize ^= maxSize;
			maxSize = minSize ^ maxSize;
			minSize ^= maxSize;
		}
		return ThreadLocalRandom.current().nextInt(minSize, maxSize);
	}

	@Override
	public boolean nullable(Config config, Definition definition) {
		int nullableProbability = config.getNullableProbability();
		if (nullableProbability < 0) {
			// 负数则忽略
			return false;
		}
		if (nullableProbability >= 100) {
			return true;
		}
		return nullableProbability >= ThreadLocalRandom.current().nextInt(100);
	}

	@Override
	public BigDecimal nextBigDecimal(Config config, Definition definition) {
		BigDecimal minBigDecimal = config.getMinBigDecimal();
		BigDecimal maxBigDecimal = config.getMaxBigDecimal();
		if (minBigDecimal == null || maxBigDecimal == null) {
			return null;
		}
		double d;
		if (minBigDecimal.compareTo(maxBigDecimal) == 0) {
			d = config.getMinBigDecimal().doubleValue();
		}
		else if (minBigDecimal.compareTo(maxBigDecimal) > 0) {
			d = ThreadLocalRandom.current().nextDouble(maxBigDecimal.doubleValue(), minBigDecimal.doubleValue());
		}
		else {
			d = ThreadLocalRandom.current().nextDouble(minBigDecimal.doubleValue(), maxBigDecimal.doubleValue());
		}
		return BigDecimal.valueOf(d).setScale(config.getBigDecimalScale(), config.getBigDecimalRoundingModel());
	}

	@Override
	public BigInteger nextBigInteger(Config config, Definition definition) {
		BigInteger minBigInterger = config.getMinBigInteger();
		BigInteger maxBigInteger = config.getMaxBigInteger();
		if (minBigInterger == null || maxBigInteger == null) {
			return null;
		}
		if (minBigInterger.compareTo(maxBigInteger) == 0) {
			return BigInteger.valueOf(config.getMinBigInteger().longValue());
		}
		else if (minBigInterger.compareTo(maxBigInteger) > 0) {
			return BigInteger.valueOf(
					ThreadLocalRandom.current().nextLong(maxBigInteger.longValue(), minBigInterger.longValue()));
		}
		return BigInteger
				.valueOf(ThreadLocalRandom.current().nextLong(minBigInterger.longValue(), maxBigInteger.longValue()));
	}

	@Override
	public Byte nextByte(Config config, Definition definition) {
		Byte minByte = config.getMinByte();
		Byte maxByte = config.getMaxByte();
		if (minByte == null || maxByte == null) {
			return null;
		}
		if (minByte.intValue() == maxByte.intValue()) {
			return minByte;
		}
		if (minByte.intValue() > maxByte.intValue()) {
			return (byte) ThreadLocalRandom.current().nextInt(maxByte, minByte);
		}
		return (byte) ThreadLocalRandom.current().nextInt(minByte, maxByte);
	}

	@Override
	public Short nextShort(Config config, Definition definition) {
		Short minShort = config.getMinShort();
		Short maxShort = config.getMaxShort();
		if (minShort == null || maxShort == null) {
			return null;
		}
		if (minShort.intValue() == maxShort.intValue()) {
			return minShort;
		}
		if (minShort.intValue() > maxShort.intValue()) {
			return (short) ThreadLocalRandom.current().nextInt(maxShort, minShort);
		}
		return (short) ThreadLocalRandom.current().nextInt(minShort, maxShort);
	}

	@Override
	public Integer nextInt(Config config, Definition definition) {
		Integer minInt = config.getMinInt();
		Integer maxInt = config.getMaxInt();
		if (minInt == null || maxInt == null) {
			return null;
		}
		if (minInt.intValue() == maxInt.intValue()) {
			return minInt;
		}
		if (minInt > maxInt) {
			return ThreadLocalRandom.current().nextInt(maxInt, minInt);
		}
		return ThreadLocalRandom.current().nextInt(minInt, maxInt);
	}

	@Override
	public Long nextLong(Config config, Definition definition) {
		Long minLong = config.getMinLong();
		Long maxLong = config.getMaxLong();
		if (minLong == null || maxLong == null) {
			return null;
		}
		if (minLong.intValue() == maxLong.intValue()) {
			return minLong;
		}
		if (minLong > maxLong) {
			return ThreadLocalRandom.current().nextLong(maxLong, minLong);
		}
		return ThreadLocalRandom.current().nextLong(minLong, maxLong);
	}

	@Override
	public Float nextFloat(Config config, Definition definition) {
		Float minFloat = config.getMinFloat();
		Float maxFloat = config.getMaxFloat();
		if (minFloat == null || maxFloat == null) {
			return null;
		}
		double f;

		if (minFloat.compareTo(maxFloat) == 0) {
			f = minFloat.doubleValue();
		}
		else if (minFloat.compareTo(maxFloat) > 0) {
			f = ThreadLocalRandom.current().nextDouble(maxFloat, minFloat);
		}
		else {
			f = ThreadLocalRandom.current().nextDouble(minFloat, maxFloat);
		}
		return BigDecimal.valueOf(f).setScale(config.getFloatScale(), config.getFloatRoundingModel()).floatValue();
	}

	@Override
	public Double nextDouble(Config config, Definition definition) {
		Double minDouble = config.getMinDouble();
		Double maxDouble = config.getMaxDouble();
		if (minDouble == null || maxDouble == null) {
			return null;
		}
		double d;
		if (minDouble.compareTo(maxDouble) == 0) {
			d = minDouble;
		}
		else if (minDouble.compareTo(maxDouble) > 0) {
			d = ThreadLocalRandom.current().nextDouble(maxDouble, minDouble);
		}
		else {
			d = ThreadLocalRandom.current().nextDouble(minDouble, maxDouble);
		}
		return BigDecimal.valueOf(d).setScale(config.getDoubleScale(), config.getDoubleRoundingModel()).doubleValue();
	}

	@Override
	public Boolean nextBoolean(Config config, Definition definition) {
		int booleanTrueProbability = config.getBooleanTrueProbability();
		if (booleanTrueProbability < 1) {
			return null;
		}
		if (booleanTrueProbability >= 100) {
			return Boolean.TRUE;
		}
		return booleanTrueProbability >= ThreadLocalRandom.current().nextInt(100);
	}

	@Override
	public Character nextCharacter(Config config, Definition definition) {
		Character minChar = config.getMinChar();
		Character maxChar = config.getMaxChar();
		if (minChar == null || maxChar == null) {
			return null;
		}
		if (minChar < 1) {
			return Character.MIN_VALUE;
		}
		if ((int) minChar == (int) maxChar) {
			return minChar;
		}
		if ((int) minChar < (int) maxChar) {
			return (char) ThreadLocalRandom.current().nextInt(maxChar, minChar);
		}
		return (char) ThreadLocalRandom.current().nextInt(minChar, maxChar);
	}

	@Override
	public String nextString(Config config, Definition definition) {
		StringModel stringMode = config.getStringMode();
		AbstractGenericGenerator abstractGenericGenerator = stringMode.getGenericGenerator();
		String str = null;
		if (abstractGenericGenerator != null) {
			str = abstractGenericGenerator.generate();
		}
		if (!Strings.isNullOrEmpty(str)) {
			return str;
		}
		int minCharSequenceLength = config.getMinCharSequenceLength();
		int maxCharSequenceLength = config.getMaxCharSequenceLength();
		if (minCharSequenceLength < 0 || maxCharSequenceLength < 0) {
			return null;
		}
		if (minCharSequenceLength == 0 || maxCharSequenceLength == 0) {
			return "";
		}
		int length;
		if (minCharSequenceLength == maxCharSequenceLength) {
			length = minCharSequenceLength;
		}
		else if (minCharSequenceLength > maxCharSequenceLength) {
			length = ThreadLocalRandom.current().nextInt(maxCharSequenceLength, minCharSequenceLength);
		}
		else {
			length = ThreadLocalRandom.current().nextInt(minCharSequenceLength, maxCharSequenceLength);
		}
		return RandomStringUtils.randomAlphanumeric(length);
	}

	@Override
	public Date nextDate(Config config, Definition definition) {
		Long minDateMillis = config.getMinDateMillis();
		Long maxDateMillis = config.getMaxDateMillis();
		if (minDateMillis == null || maxDateMillis == null) {
			return null;
		}
		if (minDateMillis < Instant.MIN.get(ChronoField.MILLI_OF_SECOND)) {
			return Date.from(Instant.MIN);
		}
		if (minDateMillis.longValue() == maxDateMillis.longValue()) {
			return new Date(minDateMillis);
		}
		long millis;
		if (minDateMillis > maxDateMillis) {
			millis = ThreadLocalRandom.current().nextLong(maxDateMillis, minDateMillis);
		}
		else {
			millis = ThreadLocalRandom.current().nextLong(minDateMillis, maxDateMillis);
		}
		return new Date(millis);
	}

}
