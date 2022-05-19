package org.magneton.support.api.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.magneton.core.Consequences;
import org.magneton.core.Response;
import org.magneton.foundation.util.Pair;
import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.sms.SendStatus;
import org.magneton.module.sms.Sms;
import org.magneton.module.statistics.Statistics;
import org.magneton.support.api.auth.constant.CommonError;
import org.magneton.support.api.auth.constant.LoginError;
import org.magneton.support.api.auth.constant.SmsError;
import org.magneton.support.api.auth.dao.intf.ApiAuthLogDao;
import org.magneton.support.api.auth.dao.intf.ApiAuthStatisticsDao;
import org.magneton.support.api.auth.dao.intf.ApiAuthUserDao;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.pojo.BaseGetSecretKeyRes;
import org.magneton.support.api.auth.pojo.BasicGetSecretKeyReq;
import org.magneton.support.api.auth.pojo.SmsAutoLoginReq;
import org.magneton.support.api.auth.pojo.SmsLoginReq;
import org.magneton.support.api.auth.pojo.SmsLoginRes;
import org.magneton.support.api.auth.pojo.SmsSendReq;
import org.magneton.support.api.auth.process.ApiAuthTokenProcessor;
import org.magneton.support.api.auth.properties.ApiAuthProperties;
import org.magneton.support.api.auth.service.AuthService;
import org.magneton.support.constant.Removed;
import org.magneton.support.constant.Status;
import org.magneton.support.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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
	private DistributedCache distributedCache;

	@Autowired
	private Statistics statistics;

	@Autowired
	private ApiAuthUserDao apiAuthUserDao;

	@Autowired
	private ApiAuthLogDao apiAuthLogDao;

	@Autowired
	private ApiAuthStatisticsDao apiAuthStatisticsDao;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired(required = false)
	private ApiAuthTokenProcessor apiAuthTokenProcessor;

	@Autowired
	private ApiAuthProperties apiAuthProperties;

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
		if (Strings.isNullOrEmpty(smsToken)) {
			return Response.response(SmsError.TOKEN_MISS);
		}
		return Response.ok(smsToken);
	}

	@Override
	public Response<SmsLoginRes> login(HttpServletRequest request, SmsLoginReq smsLoginReq) {
		String smsToken = smsLoginReq.getSmsToken();
		String mobile = smsLoginReq.getMobile();
		String smsCode = smsLoginReq.getSmsCode();
		if (!this.sms.validate(smsToken, mobile, smsCode)) {
			return Response.response(SmsError.SMS_CODE_ERROR);
		}

		ApiAuthUserDO apiAuthUser = this.apiAuthUserDao.getByMobile(mobile);
		if (apiAuthUser == null) {
			this.distributedLock.lock(mobile);
			try {
				apiAuthUser = this.apiAuthUserDao.getByMobile(mobile);
				if (apiAuthUser == null) {
					apiAuthUser = this.newApiAuthUser(mobile);
					apiAuthUser.setCreateAdditional(IpUtil.getRealIp(request));
					this.apiAuthUserDao.save(apiAuthUser);
				}
			}
			finally {
				this.distributedLock.unlock(mobile);
			}
		}
		if (apiAuthUser.getStatus() != Status.ENABLE) {
			return Response.response(LoginError.ACCOUNT_DISABLE);
		}
		return this.onLoginSuccessProcess(request, smsLoginReq.getIdentification(), mobile, apiAuthUser.getId());
	}

	@Override
	public Response<SmsLoginRes> autoLogin(HttpServletRequest request, SmsAutoLoginReq smsAutoLoginReq) {
		String identification = smsAutoLoginReq.getIdentification();
		CacheUser cacheUser = this.distributedCache.opsForValue().get("alogin:" + identification);
		if (cacheUser == null || !Objects.equal(cacheUser.getAutoLoginToken(), smsAutoLoginReq.getAutoLoginToken())) {
			return Response.response(LoginError.AUTO_LOGIN_ERROR);
		}
		return this.onLoginSuccessProcess(request, identification, cacheUser.getMobile(), cacheUser.getUserId());
	}

	@Override
	public boolean validateToken(String token, String identification) {
		Preconditions.checkNotNull(token);
		Preconditions.checkNotNull(identification);
		CacheUser cacheUser = this.distributedCache.opsForValue().get("clogin:" + token);
		if (cacheUser == null) {
			return false;
		}
		return Objects.equal(cacheUser.getIdentification(), identification);
	}

	@Override
	public Response<String> createSecretKey(BasicGetSecretKeyReq basicGetSecretKeyReq) {
		boolean secretKeyDebug = this.apiAuthProperties.isSecretKeyDebug();
		String rsaPublicKey = basicGetSecretKeyReq.getRsaPublicKey();
		String secretKeyId;
		String secretKey;
		if (!secretKeyDebug) {
			long nonce = basicGetSecretKeyReq.getNonce();
			if (System.currentTimeMillis() - this.apiAuthProperties.getSignPeriodSeconds() * 1000L < nonce) {
				return Response.response(CommonError.SECRET_TIME_OUT);
			}
			String sign = Hashing.sha256()
					.hashString(rsaPublicKey + nonce + this.apiAuthProperties.getSecretKeySignSalt(),
							StandardCharsets.UTF_8)
					.toString();
			if (!Objects.equal(sign, basicGetSecretKeyReq.getSign())) {
				return Response.response(CommonError.SECRET_SIGN_ERROR);
			}
			secretKeyId = Hashing.hmacMd5(sign.getBytes(StandardCharsets.UTF_8)).toString();
			secretKey = Hashing.sha256().hashString(sign + System.currentTimeMillis(), StandardCharsets.UTF_8)
					.toString();
		}
		else {
			secretKeyId = this.apiAuthProperties.getDebugSecretKeyId();
			secretKey = this.apiAuthProperties.getDebugSecretKey();
		}
		this.distributedCache.opsForValue().setEx("secret:" + secretKeyId, secretKey, 24 * 60 * 60);
		String encrypt = this.secretKeyEncrypt(rsaPublicKey, secretKeyId, secretKey);
		return Response.ok(encrypt);
	}

	@Override
	public String getSecretKey(String secretKeyId) {
		Preconditions.checkNotNull(secretKeyId);
		String secretKey = this.distributedCache.opsForValue().get("secret:" + secretKeyId);
		if (!Strings.isNullOrEmpty(secretKey)) {
			// 每次访问刷新Key
			this.distributedCache.expire("secret:" + secretKeyId, 24 * 60 * 60);
		}
		return secretKey;
	}

	protected String secretKeyEncrypt(String base64RsaPublicKey, String secretKeyId, String secretKey) {
		Preconditions.checkNotNull(base64RsaPublicKey);
		Preconditions.checkNotNull(secretKeyId);
		Preconditions.checkNotNull(secretKey);
		BaseGetSecretKeyRes baseGetSecretKeyRes = new BaseGetSecretKeyRes().setSecretId(secretKeyId)
				.setSecretKey(secretKey);
		RSA rsa = new RSA();
		rsa.setPublicKey(KeyUtil.generateRSAPublicKey(Base64.decode(base64RsaPublicKey)));
		return rsa.encryptBase64(JSON.toJSONBytes(baseGetSecretKeyRes), KeyType.PublicKey);
	}

	private Response<SmsLoginRes> onLoginSuccessProcess(HttpServletRequest request, String identification,
			String mobile, int useId) {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		// log
		ApiAuthLogDO apiAuthLog = new ApiAuthLogDO();
		apiAuthLog.setUserId(useId);
		apiAuthLog.setCreateAdditional(IpUtil.getRealIp(request));
		apiAuthLog.setCreateTime(System.currentTimeMillis());
		this.apiAuthLogDao.save(apiAuthLog);

		// pv uv
		boolean isUv = this.statistics.pvUv().isUv(today, useId, Duration.ofDays(1).getSeconds());
		ApiAuthStatisticsDO authStatistics = this.apiAuthStatisticsDao.getByToday(today);
		if (authStatistics == null) {
			authStatistics = new ApiAuthStatisticsDO();
			authStatistics.setPv(1);
			authStatistics.setToday(Integer.valueOf(today));
			authStatistics.setUv(1);
			try {
				this.apiAuthStatisticsDao.save(authStatistics);
			}
			catch (Throwable e) {
				// ignore.
			}
		}
		this.apiAuthStatisticsDao.increPvUv(today, isUv);

		// 缓存自动登录信息
		Pair<String, String> apiAuthToken = this.createToken(mobile);
		String token = apiAuthToken.getFirst();
		String autoLoginToken = apiAuthToken.getSecond();
		CacheUser cacheUser = new CacheUser().setUserId(useId).setAutoLoginToken(autoLoginToken).setMobile(mobile)
				.setIdentification(identification);
		this.distributedCache.opsForValue().setEx("alogin:" + identification, cacheUser, 5 * 24 * 60 * 60);
		this.distributedCache.opsForValue().setEx("clogin:" + token, cacheUser, 24 * 60 * 60);
		return Response.ok(new SmsLoginRes().setAutoLoginToken(autoLoginToken).setToken(token));
	}

	private Pair<String, String> createToken(String mobile) {
		Pair<String, String> pair = null;
		if (this.apiAuthTokenProcessor != null) {
			pair = this.apiAuthTokenProcessor.createToken(mobile);
		}
		if (pair == null) {
			String token = Hashing.sha256().hashString(UUID.randomUUID() + mobile, StandardCharsets.UTF_8).toString();
			String autoLoginToken = Hashing.sha256().hashString(UUID.randomUUID() + mobile, StandardCharsets.UTF_8)
					.toString();
			return Pair.of(token, autoLoginToken);
		}
		return pair;
	}

	@Setter
	@Getter
	@Accessors(chain = true)
	public static class CacheUser {

		private String autoLoginToken;

		private int userId;

		private String mobile;

		private String identification;

	}

	private ApiAuthUserDO newApiAuthUser(String mobile) {
		ApiAuthUserDO apiAuthUser = new ApiAuthUserDO();
		apiAuthUser.setAccount(mobile);
		apiAuthUser.setAdditional("-");
		apiAuthUser.setCreateAdditional("-");
		apiAuthUser.setCreateTime(System.currentTimeMillis());
		apiAuthUser.setPwd("-");
		apiAuthUser.setPwdSalt("-");
		apiAuthUser.setRemoved(Removed.NOT);
		apiAuthUser.setStatus(Status.ENABLE);
		return apiAuthUser;
	}

}
