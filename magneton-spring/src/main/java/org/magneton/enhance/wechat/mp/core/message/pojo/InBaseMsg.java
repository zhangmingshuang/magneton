package org.magneton.enhance.wechat.mp.core.message.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 消息基类.
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class InBaseMsg {

	/**
	 * 当前请求的APPID
	 */
	private String appId;

	/**
	 * 发送方账号（一个OpenID）
	 */
	private String fromUser;

	/**
	 * 开发者微信号
	 */
	private String toUser;

	/**
	 * 消息创建时间 （整型）
	 */
	private long createTime;

}