package org.magneton.enhance.sms.process.aliyun;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class AliyunSmsProperty {

	private String accessKeyId;

	private String accessKeySecret;

	private String endpoint = "dysmsapi.aliyuncs.com";

	/**
	 * 短信签名名称
	 * @apiNote 必须是已添加、并通过审核的短信签名。
	 *
	 * 可以登录短信服务控制台，选择国内消息或国际/港澳台消息，在签名管理页面获取。
	 * {@code https://dysms.console.aliyun.com/dysms.htm?spm=api-workbench.API%20Explorer.0.0.1e011e0fCerCB6#/overview}
	 *
	 */
	private String signName;

	/**
	 * 短信模板Code, 如 {@code SMS_154950909}
	 *
	 * @apiNote 必须是已添加、并通过审核的短信模板；且发送国际/港澳台消息时，请使用国际/港澳台短信模板。
	 */
	private String templateCode;

}
