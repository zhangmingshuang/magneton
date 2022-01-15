package org.magneton.core.secure;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/31
 */
@SuppressWarnings("UnsecureRandomNumberGeneration")
class AesTest {

	@Test
	void testPassword() throws Exception {
		for (int i = 0; i < 400; i++) {
			String data = Math.random() < 0.4 ? String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999L))
					: UUID.randomUUID().toString();
			String pwd = UUID.randomUUID().toString();
			String encrypt = Aes.encrypt(pwd, data);
			String decrypt = Aes.decrypt(pwd, encrypt);
			Assertions.assertEquals(data, decrypt, "decrypt error");
		}
	}

	@Test
	void testKey() throws Exception {
		for (int i = 0; i < 400; i++) {
			String data = Math.random() < 0.4 ? String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999L))
					: UUID.randomUUID().toString();

			SecretKey secretKey = Aes.generateKey();
			String encrypt = Aes.encrypt(secretKey, data);
			String decrypt = Aes.decrypt(secretKey, encrypt);
			Assertions.assertEquals(data, decrypt, "decrypt error");
		}
	}

}
