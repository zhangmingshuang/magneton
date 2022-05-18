package org.magneton.module.pay.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.magneton.core.base.Preconditions;

/**
 * @author zhangmsh 2022/4/7
 * @since 1.0.0
 */
public class Amounts {

	// 元分
	private static final BigDecimal YUAN_FEN = BigDecimal.valueOf(100);

	// 角分
	private static final BigDecimal JIAO_FEN = BigDecimal.valueOf(10);

	// 元角
	private static final BigDecimal YUAN_JIAO = BigDecimal.valueOf(10);

	private Amounts() {

	}

	public static Fen fromFen(int fen) {
		return new Fen(fen);
	}

	public static Jiao fromJiao(long jiao) {
		return fromJiao(BigDecimal.valueOf(jiao));
	}

	public static Jiao fromJiao(BigDecimal jiao) {
		return new Jiao(jiao);
	}

	// 分
	public static class Fen {

		private final long fen;

		public Fen(long fen) {
			this.fen = fen;
		}

		public Yuan toYuan() {
			return Yuan.fromFen(this.fen);
		}

		public Yuan toYuan(int scale) {
			return Yuan.fromFen(this.fen, scale);
		}

		public Yuan toYuan(int scale, RoundingMode roundingMode) {
			return Yuan.fromFen(this.fen, scale, roundingMode);
		}

		public Jiao toJiao() {
			return Jiao.fromFen(this.fen);
		}

		public Jiao toJiao(int scale) {
			return Jiao.fromFen(this.fen, scale);
		}

		public Jiao toJiao(int scale, RoundingMode roundingMode) {
			return Jiao.fromFen(this.fen, scale, roundingMode);
		}

		public long getValue() {
			return this.fen;
		}

		@Override
		public String toString() {
			return "Fen{" + "fen=" + this.fen + '}';
		}

	}

	// 角
	public static class Jiao {

		private final BigDecimal jiao;

		public Jiao(BigDecimal jiao) {
			this.jiao = Preconditions.checkNotNull(jiao);
		}

		public static Jiao fromFen(long fen) {
			return fromFen(fen, 2);
		}

		public static Jiao fromFen(long fen, int scale) {
			return fromFen(fen, scale, RoundingMode.HALF_EVEN);
		}

		public static Jiao fromFen(long fen, int scale, RoundingMode roundingMode) {
			return new Jiao(new BigDecimal(fen).divide(JIAO_FEN, scale, roundingMode));
		}

		public BigDecimal getValue() {
			return this.jiao;
		}

		public Fen toFen() {
			return new Fen(this.jiao.multiply(JIAO_FEN).longValue());
		}

		public Yuan toYuan() {
			return Yuan.fromJiao(this.jiao);
		}

		@Override
		public String toString() {
			return "Jiao{" + "jiao=" + this.jiao + '}';
		}

	}

	// 元
	public static class Yuan {

		private final BigDecimal yuan;

		public Yuan(BigDecimal yuan) {
			this.yuan = Preconditions.checkNotNull(yuan);
		}

		public static Yuan fromJiao(BigDecimal jiao) {
			return fromJiao(jiao, 2);
		}

		public static Yuan fromJiao(BigDecimal jiao, int scale) {
			return fromJiao(jiao, scale, RoundingMode.HALF_EVEN);
		}

		public static Yuan fromJiao(BigDecimal jiao, int scale, RoundingMode roundingMode) {
			Preconditions.checkNotNull(jiao);
			return new Yuan(jiao.divide(YUAN_JIAO, scale, roundingMode));
		}

		public static Yuan fromFen(long fen) {
			return fromFen(fen, 2);
		}

		public static Yuan fromFen(long fen, int scale) {
			return fromFen(fen, scale, RoundingMode.HALF_EVEN);
		}

		public static Yuan fromFen(long fen, int scale, RoundingMode roundingMode) {
			return new Yuan(new BigDecimal(fen).divide(YUAN_FEN, scale, roundingMode));
		}

		public Fen toFen() {
			return new Fen(this.yuan.multiply(YUAN_FEN).longValue());
		}

		public Jiao toJiao() {
			return new Jiao(this.yuan.multiply(YUAN_JIAO));
		}

		public BigDecimal getValue() {
			return this.yuan;
		}

		@Override
		public String toString() {
			return "Dollar{" + "dollar=" + this.yuan + '}';
		}

	}

}
