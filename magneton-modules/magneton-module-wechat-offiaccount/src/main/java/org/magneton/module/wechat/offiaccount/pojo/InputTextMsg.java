package org.magneton.module.wechat.offiaccount.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * 文本消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString(callSuper = true)
public class InputTextMsg extends BaseMsg {

	/**
	 * 消息id
	 */
	private Long msgId;

	/**
	 * 消息的数据ID（消息如果来自文章时才有）
	 */
	@Nullable
	private String msgDataId;

	/**
	 * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
	 */
	@Nullable
	private String idx;

	/**
	 * 消息创建时间 （整型）
	 */
	private long createTime;

}