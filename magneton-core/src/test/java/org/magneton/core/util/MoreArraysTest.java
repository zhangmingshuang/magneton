package org.magneton.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
@SuppressWarnings({ "ALL" })
class MoreArraysTest {

	@Test
	void isNullOrEmpty() {
		Assertions.assertFalse(MoreArrays.isNullOrEmpty(new int[] { 1, 2, 3 }), "not empty");
		Assertions.assertTrue(MoreArrays.isNullOrEmpty(new float[] {}), "is empty");
		Assertions.assertFalse(MoreArrays.isNullOrEmpty(new double[] { 1D }), "not empty");
		Assertions.assertTrue(MoreArrays.isNullOrEmpty(new byte[] {}), "is empty");
		Assertions.assertFalse(MoreArrays.isNullOrEmpty(new char[] { 'c' }), "not empty");
		Assertions.assertTrue(MoreArrays.isNullOrEmpty(new boolean[] {}), "is empty");
		Assertions.assertFalse(MoreArrays.isNullOrEmpty(new Integer[] { 1 }), "not empty");
		int[] a = null;
		Assertions.assertTrue(MoreArrays.isNullOrEmpty(a), "is null");
	}

	@Test
	void join() {
		String join = MoreArrays.join(new int[] { 1, 2 }, ",");
		Assertions.assertEquals("1,2", join, "arrays join error");
	}

}
