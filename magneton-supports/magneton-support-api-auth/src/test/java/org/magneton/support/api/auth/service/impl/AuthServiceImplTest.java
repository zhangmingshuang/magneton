package org.magneton.support.api.auth.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
class AuthServiceImplTest {

	private AuthServiceImpl authService = new AuthServiceImpl();

	@Test
	void test() {
		RSA rsa = new RSA();
		String publicKeyBase64 = rsa.getPublicKeyBase64();

		String secretKeyId = "123456";
		String secretKey = "123456";

		String encrypt = this.authService.secretKeyEncrypt(publicKeyBase64, secretKeyId, secretKey);

		String s = rsa.decryptStr(encrypt, KeyType.PrivateKey);

		Assertions.assertEquals("{\"secretId\":\"123456\",\"secretKey\":\"123456\"}", s);
	}

}