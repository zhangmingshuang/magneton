package org.magneton.support.api.auth.service;

import javax.servlet.http.HttpServletRequest;

import org.magneton.core.Response;
import org.magneton.support.api.auth.pojo.SmsAutoLoginReq;
import org.magneton.support.api.auth.pojo.SmsLoginReq;
import org.magneton.support.api.auth.pojo.SmsLoginRes;
import org.magneton.support.api.auth.pojo.SmsSendReq;

/**
 * .
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
public interface AuthService {

	/**
	 * 发送短信验证码
	 * @param request 请求
	 * @param smsSendReq 发送短信请求
	 * @return 短信授权，用来下一个流程操作时验证。
	 */
	Response<String> sendSms(HttpServletRequest request, SmsSendReq smsSendReq);

	/**
	 * 登录
	 * @param request 请求
	 * @param smsLoginReq 登录请求
	 * @return 登录授权，用来自动登录
	 */
	Response<SmsLoginRes> login(HttpServletRequest request, SmsLoginReq smsLoginReq);

	/**
	 * 自动登录
	 * @param request 请求
	 * @param smsAutoLoginReq 自动登录请求
	 * @return 新的自动登录授权
	 */
	Response<SmsLoginRes> autoLogin(HttpServletRequest request, SmsAutoLoginReq smsAutoLoginReq);

	boolean validateToken(String token, String identification);

}
