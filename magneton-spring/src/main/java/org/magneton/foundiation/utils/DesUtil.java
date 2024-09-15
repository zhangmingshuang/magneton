/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.foundiation.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundiation.RuntimeArgs;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密解密
 *
 * @author chenyb
 */
@Slf4j
@SuppressWarnings("unused")
public class DesUtil {

	private static final String PASSWORD_CRYPT_KEY = RuntimeArgs.sys("DES_PASSWORD").orElse("magneton");

	private static final String DES = "DES";

	private DesUtil() {
	}

	public static boolean decryptable(String data) {
		return decryptable(data, PASSWORD_CRYPT_KEY);
	}

	public static boolean decryptable(String data, String password) {
		Preconditions.checkNotNull(data, "data must not be null");
		Preconditions.checkNotNull(password, "password must not be null");
		try {
			decrypt(hex2byte(data.getBytes()), password.getBytes());
		}
		catch (Throwable e) {
			// ignore error
			return false;
		}
		return true;
	}

	/**
	 * DES解密
	 * @param data 数据
	 * @return 解密后的数据
	 */
	public static String decrypt(String data) {
		return decrypt(data, PASSWORD_CRYPT_KEY);
	}

	/**
	 * DES解密
	 * @param data 数据
	 * @param password 密码
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decrypt(String data, String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("解密的密码不能为空");
		}
		if (data == null) {
			log.info("待解密数据数据为空");
			return null;
		}
		try {
			return new String(decrypt(hex2byte(data.getBytes()), password.getBytes()));
		}
		catch (Exception e) {
			log.info("待解密数据：{}，Des解密失败：{}", data, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * DES加密
	 * @param data 要加密的数据
	 * @return 返回加密后的数据
	 */
	public static String encrypt(String data) {
		return encrypt(data, PASSWORD_CRYPT_KEY);
	}

	/**
	 * DES加密
	 * @param data 数据
	 * @param password 密码
	 * @return 返回加密后的数据
	 */
	@Nullable
	public static String encrypt(String data, String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("解密的密码不能为空");
		}
		if (data == null) {
			return null;
		}
		try {
			return byte2hex(encrypt(data.getBytes(), password.getBytes()));
		}
		catch (Exception e) {
			log.info("待加密数据：password，Des加密失败：{}", password, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception 异常
	 */
	private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		// SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		IvParameterSpec iv2 = new IvParameterSpec(key);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv2);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception 解密异常
	 */
	private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		// SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用密匙初始化Cipher对象
		IvParameterSpec iv2 = new IvParameterSpec(key);
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv2);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转字符串
	 * @param b bytes
	 * @return hex string.
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			}
			else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	private static String toStr(Object obj) {
		if (obj == null) {
			return "";
		}
		else {
			return obj.toString();
		}
	}

}