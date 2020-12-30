package org.magneton.core.security;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
@SuppressWarnings("UnsecureRandomNumberGeneration")
class AesTest {

  @ParameterizedTest
  @ValueSource(strings = {"sssssssssssssssssssssssssssssssssssssssssssssssssssss", "ss", "s", ""})
  void getBytesWithLegalSize(String iv) {
    byte[] bytesWithLegalSize = Aes.getBytesWithLegalSize(iv);
    Assertions.assertEquals(Aes.LEGAL_AES_KEYSIZES, bytesWithLegalSize.length, "site error");
  }

  @Test
  void aes128CBC() throws Exception {
    for (int i = 0; i < 400; i++) {
      String data =
          Math.random() < 0.4
              ? String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999L))
              : UUID.randomUUID().toString();
      String pwd = UUID.randomUUID().toString();
      String encrypt = Aes.aes128CBCEncrypt(data, pwd, "123");
      String decrypt = Aes.aes128CBCDecrypt(encrypt, pwd, "123");
      Assertions.assertEquals(data, decrypt, "aes128CBC encrypt decrypt error");
    }
  }

  @Test
  void aesCBCNoPadding() throws Exception {
    for (int i = 0; i < 400; i++) {
      @SuppressWarnings("NonConstantStringShouldBeStringBuffer")
      String data =
          Math.random() < 0.4
              ? String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999L))
              : UUID.randomUUID().toString();
      if (Math.random() < 0.3) {
        data += UUID.randomUUID() + UUID.randomUUID().toString();
      }
      String pwd = UUID.randomUUID().toString();
      String encrypt = Aes.aesCBCNoPaddingEncrypt(data, pwd, "123");
      String decrypt = Aes.aesCBCNoPaddingDecrypt(encrypt, pwd, "123");
      Assertions.assertEquals(data, decrypt, "aesCBCNoPadding encrypt decrypt error");
    }
  }

  @Test
  void encrypt() throws Exception {
    for (int i = 0; i < 400; i++) {
      String data =
          Math.random() < 0.4
              ? String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999L))
              : UUID.randomUUID().toString();
      String pwd = UUID.randomUUID().toString();
      String encrypt = Aes.encrypt(data, pwd);
      String decrypt = Aes.decrypt(encrypt, pwd);
      Assertions.assertEquals(data, decrypt, "encrypt decrypt error");
    }
  }
}
