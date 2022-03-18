package org.magneton.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.base.Arrays;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
@SuppressWarnings({ "ALL" })
class MoreUArrayTest {

	@Test
	void isNullOrEmpty() {
		Assertions.assertFalse(Arrays.isNullOrEmpty(new int[] { 1, 2, 3 }), "not empty");
		Assertions.assertTrue(Arrays.isNullOrEmpty(new float[] {}), "is empty");
		Assertions.assertFalse(Arrays.isNullOrEmpty(new double[] { 1D }), "not empty");
		Assertions.assertTrue(Arrays.isNullOrEmpty(new byte[] {}), "is empty");
		Assertions.assertFalse(Arrays.isNullOrEmpty(new char[] { 'c' }), "not empty");
		Assertions.assertTrue(Arrays.isNullOrEmpty(new boolean[] {}), "is empty");
		Assertions.assertFalse(Arrays.isNullOrEmpty(new Integer[] { 1 }), "not empty");
		int[] a = null;
		Assertions.assertTrue(Arrays.isNullOrEmpty(a), "is null");
	}

	@Test
	void join() {
		String join = Arrays.join(new int[] { 1, 2 }, ",");
		Assertions.assertEquals("1,2", join, "arrays join error");
	}

}
