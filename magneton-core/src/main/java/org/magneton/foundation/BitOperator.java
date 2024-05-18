package org.magneton.foundation;

/**
 * 位运算工具类.
 *
 * @author zhangmsh
 * @since M2023.9
 */
public class BitOperator {

	private BitOperator() {

	}

	/**
	 * 判断一个整数是否是奇数
	 * @param x 整数
	 * @return 是否是奇数，是返回 {@code true}，反之 {@code false}
	 */
	public static boolean isOdd(int x) {
		return (x & 1) == 1;
	}

	/**
	 * 取绝对值
	 * @apiNote 注意：以下的数字 31 是针对 int 大小为 32 而言
	 * @param x 整数
	 * @return 绝对值
	 */
	public static int abs(int x) {
		return (x ^ (x >> 31)) - (x >> 31);
	}

	/**
	 * 判断一个数是不是2的幂
	 * @param x 整数
	 * @return 是否是2的幂，是返回 {@code true}，反之 {@code false}
	 */
	public static boolean isPowerOfTwo(int x) {
		return x > 0 && (x & (x - 1)) == 0;

	}

	/**
	 * 有时候我们会将多个整数"打包"在一个整数中
	 *
	 * 一个典型的应用是将四 个字节打包为一个32位整型，那么读取的时候就需要形如"读x的第pos 位开始 的cnt位"这样的操作
	 * @param x
	 * @param pos
	 * @param cnt
	 * @return
	 */
	public static int readBit(int x, int pos, int cnt) {
		return (x >> pos) & ((1 << cnt) - 1);
	}

	/**
	 * 读取x的第pos个二进制位
	 * @param x
	 * @param pos
	 * @return
	 */
	public static int readBit(int x, int pos) {
		return (x >> pos) & 1;
	}

}