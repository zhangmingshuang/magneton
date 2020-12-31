package org.magneton.core.security;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import org.magneton.core.util.Bytes;

/**
 * Aes Utils.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/31
 */
public class Aes {

  public static final int GCM_TAG_LENGTH = 16;
  public static final int GCM_IV_LENGTH = 12;
  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final int KEY_SIZE = 128;
  private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

  private Aes() {}

  /**
   * generate secret key.
   *
   * @param password the given password.
   * @return the secret key.
   */
  public static SecretKey generateKey(@Nullable String password) {
    try {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      if (password == null || password.isEmpty()) {
        kgen.init(KEY_SIZE);
      } else {
        kgen.init(KEY_SIZE, new SecureRandom(password.getBytes(DEFAULT_CHARSET)));
      }
      return kgen.generateKey();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e.toString());
    }
  }

  /**
   * generate secret key.
   *
   * @return the secret key.
   */
  public static SecretKey generateKey() {
    return generateKey(null);
  }

  /**
   * encrypt the given plaintext.
   *
   * @param password the given password.
   * @param plaintext the given plaintext to encrypt.
   * @return the encrypted text.
   * @throws IllegalStateException if skey is not valid and GCM mode is not available in the JRE.
   */
  public static String encrypt(String password, String plaintext) {
    return encrypt(generateKey(password), plaintext);
  }

  /**
   * encrypt the given plaintext.
   *
   * @param skey secret key.
   * @param plaintext the given plaintext to encrypt.
   * @return the ciphertext.
   * @throws IllegalStateException if skey is not valid and GCM mode is not available in the JRE
   */
  public static String encrypt(SecretKey skey, String plaintext) {
    try {
      Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
      byte[] initVector = new byte[GCM_IV_LENGTH];
      (new SecureRandom()).nextBytes(initVector);
      GCMParameterSpec spec =
          new GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, initVector);
      cipher.init(Cipher.ENCRYPT_MODE, skey, spec);
      byte[] encoded = plaintext.getBytes(DEFAULT_CHARSET);
      int ivLength = initVector.length;
      byte[] ciphertext = new byte[ivLength + cipher.getOutputSize(encoded.length)];
      System.arraycopy(initVector, 0, ciphertext, 0, ivLength);
      // Perform encryption
      cipher.doFinal(encoded, 0, encoded.length, ciphertext, initVector.length);
      return Bytes.bytes2hex(ciphertext);
    } catch (NoSuchPaddingException
        | InvalidAlgorithmParameterException
        | ShortBufferException
        | BadPaddingException
        | IllegalBlockSizeException
        | InvalidKeyException
        | NoSuchAlgorithmException e) {
      /* None of these exceptions should be possible if precond is met. */
      throw new IllegalStateException(e.toString());
    }
  }

  /**
   * decrypt the given ciphertext.
   *
   * @param password the given password.
   * @param ciphertext the given chiphertext.
   * @return the plaintext.
   * @throws BadPaddingException if these indicate corrupt or malicious ciphertext. Note that
   *     AEADBadTagException may be thrown in GCM mode; this is a subclass of BadPaddingException.
   * @throws IllegalBlockSizeException if these indicate corrupt or malicious ciphertext.
   * @throws IllegalStateException if skey is not valid and GCM mode is not available in the JRE.
   */
  public static String decrypt(String password, String ciphertext)
      throws BadPaddingException, IllegalBlockSizeException {
    return decrypt(generateKey(password), ciphertext);
  }

  /**
   * decrypt the given ciphertext.
   *
   * @param skey the secret key.
   * @param ciphertext the given chiphertext.
   * @return the plaintext.
   * @throws BadPaddingException if these indicate corrupt or malicious ciphertext. Note that
   *     AEADBadTagException may be thrown in GCM mode; this is a subclass of BadPaddingException.
   * @throws IllegalBlockSizeException if these indicate corrupt or malicious ciphertext.
   * @throws IllegalStateException if skey is not valid and GCM mode is not available in the JRE.
   */
  public static String decrypt(SecretKey skey, String ciphertext)
      throws BadPaddingException, IllegalBlockSizeException {
    try {
      byte[] cipherBytes = Bytes.hex2bytes(ciphertext);
      Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
      byte[] initVector = Arrays.copyOfRange(cipherBytes, 0, GCM_IV_LENGTH);
      GCMParameterSpec spec =
          new GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, initVector);
      cipher.init(Cipher.DECRYPT_MODE, skey, spec);
      byte[] plaintext =
          cipher.doFinal(cipherBytes, GCM_IV_LENGTH, cipherBytes.length - GCM_IV_LENGTH);
      return new String(plaintext, DEFAULT_CHARSET);
    } catch (NoSuchPaddingException
        | InvalidAlgorithmParameterException
        | InvalidKeyException
        | NoSuchAlgorithmException e) {
      /* None of these exceptions should be possible if precond is met. */
      throw new IllegalStateException(e.toString());
    }
  }
}
