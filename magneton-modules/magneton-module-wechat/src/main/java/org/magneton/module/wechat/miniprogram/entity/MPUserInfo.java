package org.magneton.module.wechat.miniprogram.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/7/22
 * @since 1.0.1
 */
@Setter
@Getter
@ToString
public class MPUserInfo {

	/**
	 * 用户信息对象，不包含 openid 等敏感信息
	 */
	private UserInfo userInfo;

	/**
	 * 不包括敏感信息的原始数据字符串，用于计算签名
	 */
	private String rawData;

	/**
	 * 包括敏感数据在内的完整用户信息的加密数据，详见
	 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html#%E5%8A%A0%E5%AF%86%E6%95%B0%E6%8D%AE%E8%A7%A3%E5%AF%86%E7%AE%97%E6%B3%95
	 */
	private String encryptedData;

	/**
	 * 加密算法的初始向量
	 *
	 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html#%E5%8A%A0%E5%AF%86%E6%95%B0%E6%8D%AE%E8%A7%A3%E5%AF%86%E7%AE%97%E6%B3%95
	 */
	private String iv;

	/**
	 * 使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息，详见
	 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html
	 */
	private String signature;

	/**
	 * 敏感数据对应的云 ID，开通云开发的小程序才会返回，可通过云调用直接获取开放数据，详细见云调用直接获取开放数据
	 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html#method-cloud
	 */
	private String cloudID;

	/**
	 * https://developers.weixin.qq.com/miniprogram/dev/api/open-api/user-info/UserInfo.html
	 *
	 * @author zhangmsh 2022/7/22
	 * @since 1.0.1
	 */
	@Setter
	@Getter
	@ToString
	public static class UserInfo {

		/**
		 * 用户昵称
		 */
		private String nickName;

		/**
		 * 用户头像图片的 URL。URL 最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640x640 的正方形头像，
		 *
		 * 46 表示 46x46 的正方形头像，剩余数值以此类推。默认132），用户没有头像时该项为空。
		 *
		 * 若用户更换头像，原有头像 URL 将失效。
		 */
		private String avatarUrl;

		/**
		 * 显示 country，province，city 所用的语言。强制返回 “zh_CN”
		 */
		private String language;

	}

}
