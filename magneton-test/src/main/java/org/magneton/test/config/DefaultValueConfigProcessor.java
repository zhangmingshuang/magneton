package org.magneton.test.config;

import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.PrimitiveUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@ConfigWith(InjectType.DEFAULT_VALUE)
public class DefaultValueConfigProcessor implements ConfigProcessor {

	@Override
	public int nextSize(Config config, Definition definition) {
		return 0;
	}

	@Override
	public boolean nullable(Config config, Definition definition) {
		return false;
	}

	@Override
	public BigDecimal nextBigDecimal(Config config, Definition definition) {
		return null;
	}

	@Override
	public BigInteger nextBigInteger(Config config, Definition definition) {
		return null;
	}

	@Override
	public Byte nextByte(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (byte) 0 : null;
	}

	@Override
	public Short nextShort(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (short) 0 : null;
	}

	@Override
	public Integer nextInt(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0 : null;
	}

	@Override
	public Long nextLong(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0L : null;
	}

	@Override
	public Float nextFloat(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0.0F : null;
	}

	@Override
	public Double nextDouble(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? 0.0D : null;
	}

	@Override
	public Boolean nextBoolean(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? false : null;
	}

	@Override
	public Character nextCharacter(Config config, Definition definition) {
		return PrimitiveUtil.isPrimitive(definition.getClazz()) ? (char) 0 : null;
	}

	@Override
	public String nextString(Config config, Definition definition) {
		return null;
	}

	@Override
	public Date nextDate(Config config, Definition definition) {
		return null;
	}

}
