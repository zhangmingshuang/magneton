package org.magneton.core.security;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.magneton.core.util.Bytes;

/**
 * AES utils.
 *
 * <p>Note:AES is not a highly security algorithm.
 *
 * @author zhangmsh
 * @since 2019-04-27
 */
@SuppressWarnings({"ALL", "java:S112", "java:S5542"})
public class Aes {

  public static final int LEGAL_AES_KEYSIZES = 16;

  /** algorithm/model/padding. */
  private static final String CIPHERMODE = "AES/CBC/PKCS5Padding";
  /** algorithm/model/padding. */
  private static final String CIPHERMODE_NOPADDING = "AES/CBC/NOPadding";

  private static final char PADDING = 'x';

  private Aes() {}

  /**
   * convert the key to legal size.
   *
   * @param key decode/encode key.
   * @return the legal key bytes.
   */
  public static byte[] getBytesWithLegalSize(String key) {
    return getBytesWithLegalSize(key, StandardCharsets.UTF_8);
  }

  /**
   * convert the key to legal size.
   *
   * @param key decode/encode key.
   * @param charset the specity charset.
   * @return the legal key bytes.
   */
  public static byte[] getBytesWithLegalSize(String key, Charset charset) {
    int len = key.length();
    byte[] bytes = key.getBytes(charset);
    if (len != LEGAL_AES_KEYSIZES) {
      bytes = Arrays.copyOf(bytes, LEGAL_AES_KEYSIZES);
      // data padding.
      for (int i = len; i < LEGAL_AES_KEYSIZES; ++i) {
        bytes[i] = PADDING;
      }
    }
    return bytes;
  }
  /**
   * AES-128/CBC encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String aes128CBCEncrypt(String content, String password, String iv)
      throws Exception {
    return aes128CBCEncrypt(content, password, iv, StandardCharsets.UTF_8);
  }

  /**
   * AES-128/CBC encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @param charset the given content charset.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String aes128CBCEncrypt(String content, String password, String iv, Charset charset)
      throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHERMODE);
    cipher.init(Cipher.ENCRYPT_MODE, createSecretKeySpec(password), createIvParameterSpec(iv));
    byte[] result = cipher.doFinal(content.getBytes(charset));
    return Bytes.bytes2hex(result);
  }

  /**
   * AES-128/CBC no padding encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String aesCBCNoPaddingEncrypt(String content, String password, String iv)
      throws Exception {
    return aesCBCNoPaddingEncrypt(content, password, iv, StandardCharsets.UTF_8);
  }
  /**
   * AES-128/CBC no padding encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @param charset the given content charset.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String aesCBCNoPaddingEncrypt(
      String content, String password, String iv, Charset charset) throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHERMODE_NOPADDING);
    cipher.init(Cipher.ENCRYPT_MODE, createSecretKeySpec(password), createIvParameterSpec(iv));
    byte[] bytes = content.getBytes(charset);
    int plaintextLength = bytes.length;
    plaintextLength = nextSize(plaintextLength < 16 ? 15 : plaintextLength - 1);
    if (plaintextLength > bytes.length) {
      bytes = Arrays.copyOf(bytes, plaintextLength);
    }
    byte[] result = cipher.doFinal(bytes);
    return Bytes.bytes2hex(result);
  }
  /**
   * AES-128/CBC decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @return the decrypt data.
   * @throws Exception if is bad.
   */
  public static String aes128CBCDecrypt(String content, String password, String iv)
      throws Exception {
    return aes128CBCDecrypt(content, password, iv, StandardCharsets.UTF_8);
  }
  /**
   * AES-128/CBC decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @param charset the given content charset.
   * @return the decrypt data.
   * @throws Exception if is bad.
   */
  public static String aes128CBCDecrypt(String content, String password, String iv, Charset charset)
      throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHERMODE);
    cipher.init(Cipher.DECRYPT_MODE, createSecretKeySpec(password), createIvParameterSpec(iv));
    byte[] bytes = Bytes.hex2bytes(content);
    byte[] result = cipher.doFinal(bytes);
    return new String(result, charset);
  }
  /**
   * AES-128/CBC no padding decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @return the decrypt data.
   * @throws Exception if is bad.
   */
  public static String aesCBCNoPaddingDecrypt(String content, String password, String iv)
      throws Exception {
    return aesCBCNoPaddingDecrypt(content, password, iv, StandardCharsets.UTF_8);
  }
  /**
   * AES-128/CBC no padding decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param iv initial vector.
   * @param charset the given content charset.
   * @return the decrypt data.
   * @throws Exception if is bad.
   */
  public static String aesCBCNoPaddingDecrypt(
      String content, String password, String iv, Charset charset) throws Exception {
    Cipher cipher = Cipher.getInstance(CIPHERMODE_NOPADDING);
    cipher.init(Cipher.DECRYPT_MODE, createSecretKeySpec(password), createIvParameterSpec(iv));
    byte[] bytes = Bytes.hex2bytes(content);
    byte[] result = cipher.doFinal(bytes);
    int binary = result.length >> 2;
    byte bb = result[binary];
    if (bb == 0) {
      while (bb == 0) {
        bb = result[--binary];
      }
    } else {
      while (bb != 0) {
        bb = result[++binary];
      }
      --binary;
    }
    return new String(result, 0, binary + 1, charset);
  }
  /**
   * encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String encrypt(String content, String password) throws Exception {
    return encrypt(content, password, StandardCharsets.UTF_8);
  }
  /**
   * encrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param charset the given content charset.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String encrypt(String content, String password, Charset charset) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128, new SecureRandom(password.getBytes(charset)));
    byte[] bytes = kgen.generateKey().getEncoded();
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(bytes, "AES"));
    byte[] result = cipher.doFinal(content.getBytes(charset));
    return Bytes.bytes2hex(result);
  }

  /**
   * decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String decrypt(String content, String password) throws Exception {
    return decrypt(content, password, StandardCharsets.UTF_8);
  }
  /**
   * decrypt.
   *
   * @param content the given encrypting content.
   * @param password the encrypting content password.
   * @param charset the given content charset.
   * @return the encrypt data.
   * @throws Exception if is bad.
   */
  public static String decrypt(String content, String password, Charset charset) throws Exception {
    byte[] bytes = Bytes.hex2bytes(content);
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128, new SecureRandom(password.getBytes()));
    SecretKey secretKey = kgen.generateKey();
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] result = cipher.doFinal(bytes);
    return new String(result, charset);
  }

  private static int nextSize(int i) {
    int newCapacity = i;
    newCapacity |= newCapacity >>> 1;
    newCapacity |= newCapacity >>> 2;
    newCapacity |= newCapacity >>> 4;
    newCapacity |= newCapacity >>> 8;
    newCapacity |= newCapacity >>> 16;
    newCapacity++;
    return newCapacity;
  }

  /**
   * create secret key spec.
   *
   * @param key encode key.
   * @return SecretKeySpec
   */
  private static SecretKeySpec createSecretKeySpec(String key) {
    return new SecretKeySpec(getBytesWithLegalSize(key), "AES");
  }

  /**
   * create initial vector paramter spec.
   *
   * @param iv initial vecto.
   * @return IvParameterSpec
   */
  private static IvParameterSpec createIvParameterSpec(String iv) {
    return new IvParameterSpec(getBytesWithLegalSize(iv));
  }
}
