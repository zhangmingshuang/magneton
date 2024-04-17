package org.magneton.module.wechat.offiaccount.pojo;

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
public class BaseMsg {

	/**
	 * 发送方账号（一个OpenID）
	 */
	private String fromUserName;

	/**
	 * 开发者微信号
	 */
	private String toUserName;

	/**
	 * 消息创建时间 （整型）
	 */
	private long createTime;

}