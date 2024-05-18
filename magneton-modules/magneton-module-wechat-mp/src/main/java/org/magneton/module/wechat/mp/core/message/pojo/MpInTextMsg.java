package org.magneton.module.wechat.mp.core.message.pojo;

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
public class MpInTextMsg extends InBaseMsg {

	/**
	 * 消息id
	 */
	private Long msgId;

	/**
	 * 文本消息内容
	 */
	private String content;

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

}