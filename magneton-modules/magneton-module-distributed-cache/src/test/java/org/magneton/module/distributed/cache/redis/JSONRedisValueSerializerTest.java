package org.magneton.module.distributed.cache.redis;

import java.io.Serializable;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
class JSONRedisValueSerializerTest {

	private JSONRedisValueSerializer serializer = new JSONRedisValueSerializer();

	@Test
	void test_string() {
		byte[] gets = this.serializer.serialize("get");
		String deserialize = this.serializer.deserialize(gets, String.class);
		System.out.println(deserialize);
		Assertions.assertEquals(deserialize, "get");
	}

	@Test
	void test_obj() {
		A a = ChaosTest.createExcepted(A.class);
		byte[] bytes = this.serializer.serialize(a);
		A a1 = this.serializer.deserialize(bytes, A.class);
		Human.sout(a, a1);
		Assertions.assertTrue(ChaosTest.booleanSupplier().valueEquals(a, a1));
	}

	@Data
	public static class A implements Serializable {

		private int i;

		private String s;

		private A a;

	}

}