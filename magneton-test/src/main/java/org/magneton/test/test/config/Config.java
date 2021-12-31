package org.magneton.test.test.config;

import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.model.StringModel;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Config {

	public static final int DEFAULT_NUMBER_MIN = -128;

	public static final int DEFAULT_NUMBER_MAX = 127;

	/** 生成数组、集合时的随机最小长度,如果小于0表示返回{@code null}，如果等于0表示返回空数组、字符串、集合 */
	private int minSize = 1;

	/** 生成数组、集合时的随机最大长度,如果小于0表示返回{@code null}，如果等于0表示返回空数组、字符串、集合 */
	private int maxSize = 10;

	/** 生成字符串时的随机最小长度，如果小于0表示返回{@code null}，如果为0表示返回空字符串 */
	private int minCharSequenceLength = 1;

	/** 生成字符串时的随机最大长度，如果小于0表示返回{@code null}，如果为0表示返回空字符串 */
	private int maxCharSequenceLength = 16;

	/**
	 * 生成字符串时的随机最小长度，如果小于0表示返回{@code null}，如果为0表示返回空字符串
	 */
	private int minStringLength = 1;

	/**
	 * 生成字符串时的随机最大长度，如果小于0表示返回{@code null}，如果为0表示返回空字符串
	 */
	private int maxStringLength = 16;

	/** 生成byte的最小值。如果设置为{@code null}表示返回{@code null} */
	private Byte minByte = (byte) DEFAULT_NUMBER_MIN;

	/** 生成byte的最大值。如果设置为{@code null}表示返回{@code null} */
	private Byte maxByte = (byte) DEFAULT_NUMBER_MAX;

	/** 生成Short的最小值。如果设置为{@code null}表示返回{@code null} */
	private Short minShort = (short) DEFAULT_NUMBER_MIN;

	/** 生成Short的最小值。如果设置为{@code null}表示返回{@code null} */
	private Short maxShort = (short) DEFAULT_NUMBER_MAX;

	/** 生成随机Int类型值时的最小值，如果设置为{@code null}表示返回{@code null} */
	private Integer minInt = DEFAULT_NUMBER_MIN;

	/** 生成随机Int类型值时的最大值，如果设置为{@code null}表示返回{@code null} */
	private Integer maxInt = DEFAULT_NUMBER_MAX;

	/** 生成随机Long类型值时的最小值，如果设置为{@code null}表示返回{@code null} */
	private Long minLong = (long) DEFAULT_NUMBER_MIN;

	/** 生成随机Long类型值时的最大值，如果设置为{@code null}表示返回{@code null} */
	private Long maxLong = (long) DEFAULT_NUMBER_MAX;

	/** 生成随机Float类型值时的最小值，如果设置为{@code null}表示返回{@code null} */
	private Float minFloat = DEFAULT_NUMBER_MIN * 1.0F;

	/** 生成随机Float类型值时的最大值，如果设置为{@code null}表示返回{@code null} */
	private Float maxFloat = DEFAULT_NUMBER_MAX * 1.0F;

	/** 设置Float类型的小数点保留位数 */
	private int floatScale = 2;

	/** 设置Float的小数点舍入模式 */
	private RoundingMode floatRoundingModel = RoundingMode.HALF_UP;

	/** 生成随机Double类型值时的最小值，如果设置为{@code null}表示返回{@code null} */
	private Double minDouble = DEFAULT_NUMBER_MIN * 1.0D;

	/** 生成随机Double类型值时的最大值，如果设置为{@code null}表示返回{@code null} */
	private Double maxDouble = DEFAULT_NUMBER_MAX * 1.0D;

	/** 设置Doube类型的小数点保留位数 */
	private int doubleScale = 2;

	/** 设置Double的小数点舍入模式 */
	private RoundingMode doubleRoundingModel = RoundingMode.HALF_UP;

	/** 生成随机Char时的最小值，如果设置为{@code null}表示返回{@code null} */
	private Character minChar = Character.MIN_VALUE;

	/** 生成随机Char时的最大值，如果设置为{@code null}表示返回{@code null} */
	private Character maxChar = Character.MAX_VALUE;

	/** 设置BigInteger类型的最小值，如果设置为{@code null}，则生成的一定是 {@code null} */
	private BigInteger minBigInteger = BigInteger.valueOf(DEFAULT_NUMBER_MIN);

	/** 设置BigInteger类型的最大值，如果设置为{@code null}，则生成的一定是 {@code null} */
	private BigInteger maxBigInteger = BigInteger.valueOf(DEFAULT_NUMBER_MAX);

	/** 设置BigDecimal类型的最小值，如果设置为{@code null}，则生成的一定是 {@code null} */
	private BigDecimal minBigDecimal = BigDecimal.valueOf(DEFAULT_NUMBER_MIN);

	/** 设置BigDecimal类型的最大值，如果设置为{@code null}，则生成的一定是 {@code null} */
	private BigDecimal maxBigDecimal = BigDecimal.valueOf(DEFAULT_NUMBER_MAX);

	/** 设置BigDecimal类型的小数点保留位数 */
	private int bigDecimalScale = 2;

	/** 设置BigDecimal的小数点舍入模式 */
	private RoundingMode bigDecimalRoundingModel = RoundingMode.HALF_UP;

	/**
	 * 生成日期型数据时的最小时间，如果设置为{@code null}，则生成的一定是 {@code null}
	 *
	 * @see java.util.Date
	 * @see java.sql.Date
	 * @see java.util.Calendar
	 * @see Instant
	 * @see java.time.LocalDate
	 * @see java.time.LocalDateTime
	 * @see java.time.LocalTime
	 * @see java.time.MonthDay
	 * @see java.time.OffsetDateTime
	 * @see java.time.Year
	 * @see java.time.YearMonth
	 * @see java.time.ZonedDateTime
	 * @see java.time.chrono.HijrahDate
	 * @see java.time.chrono.JapaneseDate
	 * @see java.time.chrono.MinguoDate
	 * @see java.time.chrono.ThaiBuddhistDate
	 */
	private Long minDateMillis = System.currentTimeMillis();

	/** 生成日期型数据时的最大时间,如果设置为{@code null}，则生成的一定是 {@code null} */
	private Long maxDateMillis = System.currentTimeMillis();

	/**
	 * 设置Boolean类型的 {@code true} 生成概率。
	 *
	 * <ul>
	 * <li>100表示一定是{@code true}。
	 * <li>0表示一定是{@code false}，
	 * <li>小于0表示一定是{@code null}
	 * </ul>
	 */
	private int booleanTrueProbability = 50;

	/**
	 * 生成数据是否允许为 {@code null} 的概率，
	 *
	 * <ul>
	 * <li>小于0表示不管任何数据生成模式{@link InjectType}都会尽可能的生成数据，也就是会忽略该判断
	 * <li>0 表示一定不为 {@code null}
	 * <li>100 表示一定为{@code null}
	 * </ul>
	 */
	private int nullableProbability = -1;

	/** 生成字符串的模式 */
	private StringModel stringMode = StringModel.NORMAL;

	@SneakyThrows
	public static Config copyOf(Config config) {
		Preconditions.checkNotNull(config, "config must be not null");
		Field[] fields = Config.class.getDeclaredFields();
		Config copied = new Config();
		for (Field field : fields) {
			if (field.getDeclaringClass() == Object.class || Modifier.isFinal(field.getModifiers())
					|| Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			Object value = field.get(config);
			field.set(copied, value);
		}
		return copied;
	}

	public void setAllNumberMinValue(Number number) {
		this.setMinByte(number.byteValue());
		this.setMinShort(number.shortValue());
		this.setMinInt(number.intValue());
		this.setMinLong(number.longValue());
		this.setMinFloat(number.floatValue());
		this.setMinDouble(number.doubleValue());
		this.setMinChar((char) number.intValue());
		if (number instanceof BigDecimal) {
			this.setMinBigDecimal((BigDecimal) number);
		}
		else {
			this.setMinBigDecimal(BigDecimal.valueOf(number.doubleValue()));
		}
		if (number instanceof BigInteger) {
			this.setMinBigInteger((BigInteger) number);
		}
		else {
			this.setMinBigInteger(BigInteger.valueOf(number.longValue()));
		}
	}

	public void setAllNumberMaxValue(Number number) {
		this.setMaxByte(number.byteValue());
		this.setMaxShort(number.shortValue());
		this.setMaxInt(number.intValue());
		this.setMaxLong(number.longValue());
		this.setMaxFloat(number.floatValue());
		this.setMaxDouble(number.doubleValue());
		this.setMaxChar((char) number.intValue());
		if (number instanceof BigDecimal) {
			this.setMaxBigDecimal((BigDecimal) number);
		}
		else {
			this.setMaxBigDecimal(BigDecimal.valueOf(number.doubleValue()));
		}
		if (number instanceof BigInteger) {
			this.setMaxBigInteger((BigInteger) number);
		}
		else {
			this.setMaxBigInteger(BigInteger.valueOf(number.longValue()));
		}
	}

	public static Config builder() {
		return new Config();
	}

	public Config build() {
		return Config.copyOf(this);
	}

}
