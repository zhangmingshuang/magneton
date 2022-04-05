package org.magneton.module.pay.wechat.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信支付回调请求
 *
 * {@code https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_5.shtml}
 *
 * @author zhangmsh 2022/4/4
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WechatPayCallbackRes {

	/**
	 * 通知ID string[1,36] 是 通知的唯一ID 示例值：EV-2018022511223320873
	 **/
	private String id;

	/**
	 * 通知创建时间 string[1,32] 是
	 * 通知创建的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
	 * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。
	 * 示例值：2015-05-20T13:29:35+08:00
	 **/
	@JsonProperty("create_time")
	private String createTime;

	/**
	 * 通知类型 string[1,32] 是 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS 示例值：TRANSACTION.SUCCESS
	 **/
	@JsonProperty("event_type")
	private String eventType;

	/**
	 * 通知数据类型 string[1,32] 是 通知的资源数据类型，支付成功通知为encrypt-resource 示例值：encrypt-resource
	 **/
	@JsonProperty("resource_type")
	private String resourceType;

	/** 通知数据 object 是 通知资源数据 **/
	private Resource resource;

	/**
	 * 回调摘要 string[1,64] 是 回调摘要 示例值：支付成功
	 **/
	private String summary;

	@Setter
	@Getter
	@ToString
	public static class Resource {

		/**
		 * 加密算法类型 string[1,32] 是 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
		 * 示例值：AEAD_AES_256_GCM
		 **/
		private String algorithm;

		/**
		 * 数据密文 string[1,1048576] 是 Base64编码后的开启/停用结果数据密文 示例值：sadsadsadsad
		 **/
		private String ciphertext;

		/**
		 * 附加数据 string[1,16] 否 附加数据 示例值：fdasfwqewlkja484w
		 **/
		@Nullable
		@JsonProperty("associated_data")
		private String associatedData;

		/**
		 * 原始类型 string[1,16] 是 原始回调类型，为transaction 示例值：transaction
		 **/
		@JsonProperty("original_type")
		private String originalType;

		/**
		 * 随机串 string[1,16] 是 加密使用的随机串 示例值：fdasflkja484w
		 **/
		private String nonce;

	}

}
