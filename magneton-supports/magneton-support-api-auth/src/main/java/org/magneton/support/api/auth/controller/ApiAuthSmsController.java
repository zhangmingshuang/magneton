package org.magneton.support.api.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.magneton.core.Response;
import org.magneton.support.api.auth.constant.SmsError;
import org.magneton.support.api.auth.pojo.SmsLoginReq;
import org.magneton.support.api.auth.pojo.SmsSendReq;
import org.magneton.support.api.auth.service.AuthService;
import org.magneton.support.doc.HtmlDoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信注册登录模式.
 *
 * @apiNote 流程说明：
 *
 * 1、未登录时
 * <ul>
 * <li>1.1 发送短信，获得授权</li>
 * <li>1.2 调用登录，获得自动登录授权</li>
 * </ul>
 *
 * 2. 已登录时 登录成功之后会获得自动登录授权码，可以使用登录授权码自动登录。
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@RestController
@RequestMapping("/${magneton.api.prefix:api}/auth/sms")
@Validated
public class ApiAuthSmsController {

	static {
		HtmlDoc.addApi("短信注册登录");
	}

	@Autowired
	private AuthService authService;

	/**
	 * 发送短信
	 * @apiNote 发送短信有对应的风控模块，如果返回 {@link SmsError#MOBILE_RISK}的状态码， 调用方需要进行风险控制流程的接入。
	 * 如果发送短信返回
	 * {@link SmsError#MOBIL_SEND_GAP}的状态码，调用方需要等待要的时间之后才可以继续调用。在该状态码时，返回的数据{@code data}中表示为需要等待的秒数。
	 * @param request 请求
	 * @param smsSendReq 短信发送请求
	 * @return 校验授权码. 用来进行下一步操作时携带校验。
	 */
	@PostMapping("/send")
	public Response<String> send(HttpServletRequest request, @Valid @RequestBody SmsSendReq smsSendReq) {
		return this.authService.sendSms(request, smsSendReq);
	}

	/**
	 * 登录
	 * @apiNote 该流程为第二步流程，需要先获取验证码之后才可以进行登录。
	 * @param request 请求
	 * @param smsLoginReq 登录请求
	 * @return 自动登录授权，可以用来调用自动登录流程。
	 */
	@PostMapping("/login")
	public Response<String> login(HttpServletRequest request, @Valid @RequestBody SmsLoginReq smsLoginReq) {
		return this.authService.login(request, smsLoginReq);
	}

}
