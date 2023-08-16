package org.magneton.module.wechat.miniprogram.core;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.codec.Base64;

/**
 * 微信小程序Aes数据加解密辅助类
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class MPAesHelper {

	private MPAesHelper() {
		// private
	}

	/**
	 * 微信小程序 开放数据解密 AES解密（Base64） Add by 成长的小猪（Jason.Song） on 2018/10/26
	 * @param encryptedData 已加密的数据
	 * @param sessionKey 解密密钥
	 * @param iv IV偏移量
	 * @return
	 * @throws Exception
	 */
	public static String decryptForWeChatApplet(String encryptedData, String sessionKey, String iv) throws Exception {
		byte[] decryptBytes = Base64.decode(encryptedData);
		byte[] keyBytes = Base64.decode(sessionKey);
		byte[] ivBytes = Base64.decode(iv);

		return new String(decryptByAesBytes(decryptBytes, keyBytes, ivBytes));
	}

	/**
	 * AES解密 Add by 成长的小猪（Jason.Song） on 2018/10/26
	 * @param decryptedBytes 待解密的字节数组
	 * @param keyBytes 解密密钥字节数组
	 * @param ivBytes IV初始化向量字节数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByAesBytes(byte[] decryptedBytes, byte[] keyBytes, byte[] ivBytes) throws Exception {
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] outputBytes = cipher.doFinal(decryptedBytes);
		return outputBytes;
	}

}