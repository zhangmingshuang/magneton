package org.magneton.support.api.auth.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.magneton.core.ResponseException;
import org.magneton.core.base.Arrays;
import org.magneton.core.base.Strings;
import org.magneton.core.collect.Maps;
import org.magneton.module.safedog.SignSafeDog;
import org.magneton.support.adapter.InterceptorAdapter;
import org.magneton.support.api.auth.constant.CommonError;
import org.magneton.support.api.auth.properties.ApiAuthProperties;
import org.magneton.support.api.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
@Component
@Slf4j
public class SignInterceptor implements InterceptorAdapter {

	@Autowired
	private ApiAuthProperties apiAuthProperties;

	@Autowired
	private SignSafeDog signSafeDog;

	@Autowired
	private AuthService authService;

	@Override
	public HandlerInterceptorAdapter handlerInterceptorAdapter() {
		if (!this.apiAuthProperties.isSign()) {
			return null;
		}
		return new SignHandlerInterceptorAdapter();
	}

	@Override
	public Set<String> pathPatterns() {
		return this.merge(this.apiAuthProperties.getSignInterceptorPathPatterns(), "/api/**");
	}

	@Nullable
	@Override
	public Set<String> excludePathPatterns() {
		return this.merge(this.apiAuthProperties.getSignInterceptorExcludePathPatterns(),
				"/api/auth/basic/getSecretKey");
	}

	private class SignHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			final ApiAuthProperties properties = SignInterceptor.this.apiAuthProperties;
			String signParamName = properties.getSignParamName();
			String sign = request.getParameter(signParamName);
			if (sign == null) {
				throw new ResponseException(CommonError.SIGN_PARAM_MISS);
			}
			String nonce = request.getParameter(properties.getNonceParamName());
			try {
				if (Strings.isNullOrEmpty(nonce) || Long.parseLong(nonce)
						+ properties.getSignInterceptorNonceGapLimitSeconds() * 1000L < System.currentTimeMillis()) {
					throw new ResponseException(CommonError.TIME_OUT);
				}
			}
			catch (NumberFormatException e) {
				throw new ResponseException(CommonError.TIME_OUT);
			}
			String secretKeyId = request.getParameter(properties.getSecretKeyIdParamName());
			if (Strings.isNullOrEmpty(secretKeyId)) {
				throw new ResponseException(CommonError.SECRET_KEY_ID_PARAM_MISS);
			}
			String secretKey = SignInterceptor.this.authService.getSecretKey(secretKeyId);
			if (Strings.isNullOrEmpty(secretKey)) {
				throw new ResponseException(CommonError.SECRET_KEY_MISS);
			}
			Enumeration<String> parameterNames = request.getParameterNames();
			Map<String, String> data = Maps.newHashMapWithExpectedSize(8);
			while (parameterNames.hasMoreElements()) {
				String key = parameterNames.nextElement();
				String value = request.getParameter(key);
				data.put(key, value);
			}
			String[] signInterceptorNeedParams = SignInterceptor.this.apiAuthProperties.getSignInterceptorNeedParams();
			if (!Arrays.isNullOrEmpty(signInterceptorNeedParams)) {
				for (String signInterceptorNeedParam : signInterceptorNeedParams) {
					if (!data.containsKey(signInterceptorNeedParam)) {
						log.error("sign need param {} miss", signInterceptorNeedParam);
						throw new ResponseException(CommonError.SIGN_NEED_PARAM_MISS);
					}
				}
			}
			data.remove(signParamName);
			if (!SignInterceptor.this.signSafeDog.validate(sign,
					SignInterceptor.this.apiAuthProperties.getSignPeriodSeconds(), data, secretKey)) {
				throw new ResponseException(CommonError.SIGN_ERROR);
			}
			return true;
		}

	}

}
