package org.magneton.support.api.auth.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.magneton.core.Consequences;
import org.magneton.core.Response;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.sms.SendStatus;
import org.magneton.module.sms.Sms;
import org.magneton.support.api.auth.constant.SmsError;
import org.magneton.support.api.auth.dao.intf.ApiAuthUserDao;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.pojo.SmsLoginReq;
import org.magneton.support.api.auth.pojo.SmsSendReq;
import org.magneton.support.api.auth.service.AuthService;
import org.magneton.support.constant.Removed;
import org.magneton.support.constant.Status;
import org.magneton.support.util.IpUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private Sms sms;

	@Autowired
	private DistributedLock distributedLock;

	@Autowired
	private ApiAuthUserDao apiAuthUserDao;

	@Override
	public Response<String> sendSms(HttpServletRequest request, SmsSendReq smsSendReq) {
		String mobile = smsSendReq.getMobile();
		String group = IpUtil.getRealIp(request);
		Consequences<SendStatus> sendConsequences = this.sms.trySend(mobile, group);
		if (!sendConsequences.isSuccess()) {
			SendStatus sendStatus = sendConsequences.getData();
			if (sendStatus == SendStatus.RISK) {
				return Response.bad().code(SmsError.MOBILE_RISK.code()).message("短信发送条数过多，请稍后重试");
			}
			if (sendStatus == SendStatus.SEND_GAP) {
				// 在发送间隔中
				long ttl = this.sms.ttl(mobile);
				return Response.bad(String.valueOf(ttl)).code(SmsError.MOBIL_SEND_GAP.code())
						.message("需要等待" + ttl + "秒后继续发送");
			}
			return Response.bad().message(sendConsequences.getMessage());
		}
		String smsToken = this.sms.token(mobile, group);
		return Response.ok(smsToken);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Response<String> login(HttpServletRequest request, SmsLoginReq smsLoginReq) {
		String smsToken = smsLoginReq.getSmsToken();
		String mobile = smsLoginReq.getMobile();
		String smsCode = smsLoginReq.getSmsCode();
		if (!this.sms.validate(smsToken, mobile, smsCode)) {
			return Response.response(SmsError.SMS_CODE_ERROR);
		}
		this.distributedLock.lock(mobile);
		try {
			ApiAuthUserDO apiAuthUser = this.apiAuthUserDao.getByMobile(mobile);
			if (apiAuthUser == null) {
				apiAuthUser = this.newApiAuthUser(mobile);
				this.apiAuthUserDao.save(apiAuthUser);
			}
			else {
				// 已存在
			}
		}
		finally {
			this.distributedLock.unlock(mobile);
		}
		return null;
	}

	private ApiAuthUserDO newApiAuthUser(String mobile) {
		ApiAuthUserDO apiAuthUser = new ApiAuthUserDO();
		apiAuthUser.setAccount(mobile);
		apiAuthUser.setAdditional("");
		apiAuthUser.setCreateAdditional("");
		apiAuthUser.setCreateTime(System.currentTimeMillis());
		apiAuthUser.setPwd("-");
		apiAuthUser.setPwdSalt("-");
		apiAuthUser.setRemoved(Removed.NOT);
		apiAuthUser.setStatus(Status.ENABLE);
		return apiAuthUser;
	}

}
