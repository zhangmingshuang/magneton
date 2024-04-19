package org.magneton.module.wechat.mp.core.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 消息体.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString
public class MpMsgBody {

	/**
	 * 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 */
	private String signature;

	/**
	 * 时间戳
	 */
	private String timestamp;

	/**
	 * 随机数
	 */
	private String nonce;

	/**
	 * 随机字符串
	 */
	private String echostr;

	/**
	 * 加密类型
	 * <ul>
	 * <li>raw：明文</li>
	 * <li>aes：aes加密</li>
	 * </ul>
	 */
	private String encryptType;

	/**
	 * 消息请求数据
	 */
	private String requestBody;

}