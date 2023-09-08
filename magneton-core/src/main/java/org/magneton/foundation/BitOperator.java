package org.magneton.foundation;

/**
 * .
 *
 * @author zhangmsh 2022/1/13
 * @since 1.2.0
 */
public class BitOperator {

	private BitOperator() {

	}

	/**
	 * 有时候我们会将多个整数"打包"在一个整数中，一个典型的应用是将四 个字节打包为一个32位整型，那么读取的时候就需要形如"读x的第pos 位开始 的cnt位"这样的操作
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
