package org.magneton.support.api.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.magneton.core.Response;
import org.magneton.support.api.auth.pojo.SmsSendReq;
import org.magneton.support.doc.HtmlDoc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信注册登录模式.
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
@RestController
@RequestMapping("/${magneton.api.prefix:api}/auth/sms")
public class ApiAuthSmsController {

	static {
		HtmlDoc.addApi("短信注册登录");
	}

	/**
	 * 发送短信
	 * @apiNote 发送短信有对应的风控模块，如果返回
	 * {@link org.magneton.support.api.auth.constant.SmsError#MOBILE_RISK}的状态码，
	 * 调用方需要进行风险控制流程的接入。
	 * @param request 请求
	 * @param smsSendReq 短信发送请求
	 * @return 校验授权码.
	 */
	@PostMapping("/send")
	public Response<String> send(HttpServletRequest request, SmsSendReq smsSendReq) {
		return null;
	}

}
