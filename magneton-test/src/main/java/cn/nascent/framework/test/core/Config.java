package cn.nascent.framework.test.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {

	/**
	 * 生成随机数组、字符串、集合时的随机最小长度
	 */
	private int minSize = 1;

	/**
	 * 生成随机数组、字符串、集合时的随机最大长度
	 */
	private int maxSize = 10;

	/**
	 * 生成随机Int类型值时的最小值
	 */
	private int minInt = -128;

	/**
	 * 生成随机Int类型值时的最大值（不包括）
	 */
	private int maxInt = 127;

	/**
	 * 生成随机Long类型值时的最小值
	 */
	private int minLong = -128;

	/**
	 * 生成随机Long类型值时的最大值（不包括）
	 */
	private int maxLong = Integer.MAX_VALUE;

	private double minDouble = 0;

	private double maxDouble = 127;

	private int doubleScale = 2;

	private RoundingMode doubleRoundingModel = RoundingMode.HALF_UP;

	private float minFloat = 0;

	private float maxFloat = 127;

	private int floatScale = 2;

	private RoundingMode floatRoundingModel = RoundingMode.HALF_UP;

	private byte minByte = 0;

	private byte maxByte = 127;

	private short minShort = 0;

	private short maxShort = 127;

	private BigInteger minBigInterger = BigInteger.ZERO;

	private BigInteger maxBigInteger = BigInteger.valueOf(127);

	private BigDecimal minBigDecimal = BigDecimal.ZERO;

	private BigDecimal maxBigDecimal = BigDecimal.valueOf(127);

	private int bigDecimalScale = 2;

	private RoundingMode bigDecimalRoundingModel = RoundingMode.HALF_UP;

	/**
	 * 在 {@link cn.nascent.framework.test.injector.InjectType#DEMON}模式时，创建{@code null}值的概念
	 * (0~100)
	 *
	 * <p>
	 * 如果是基本类型，则返回对应基本类型的最小值！！
	 */
	private int demonNullProbability = 50;

}
