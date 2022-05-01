package org.magneton.module.im.tencent.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 发送消息响应.
 *
 * https://cloud.tencent.com/document/product/269/1612
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class MsgSendRes {

	/**
	 * ActionStatus String 请求处理的结果，OK 表示处理成功，FAIL 表示失败
	 */
	@JsonProperty("ActionStatus")
	private String actionStatus;

	/**
	 * ErrorCode Integer 本次请求的错误码 如有任意帐号发送成功，则此字段返回0 全部帐号都发送失败，则此字段返回非0
	 */
	@JsonProperty("ErrorCode")
	private int errorCode;

	/**
	 * ErrorInfo String 详细错误信息
	 */
	@Nullable
	@JsonProperty("ErrorInfo")
	private String errorInfo;

	/**
	 * ErrorList Array 发消息失败的帐号列表，在此列表中的目标帐号，消息发送失败或帐号不存在。若消息全部发送成功，则 ErrorList 为空
	 */
	@Nullable
	@JsonProperty("ErrorList")
	private List<Error> errors;

	/**
	 * MsgKey String 消息唯一标识，用于撤回。长度不超过50个字符
	 */
	@Nullable
	@JsonProperty("MsgKey")
	private String msgKey;

	@Setter
	@Getter
	@ToString
	public static class Error {

		/**
		 * ErrorList.To_Account String 消息发送失败的目标帐号
		 */
		@JsonProperty("To_Account")
		private String toAccount;

		/**
		 * ErrorList.ErrorCode Integer 消息发送失败的错误码，若目标帐号的错误码为70107表示该帐号不存在
		 */
		@JsonProperty("ErrorCode")
		private int errorCode;

	}

}
