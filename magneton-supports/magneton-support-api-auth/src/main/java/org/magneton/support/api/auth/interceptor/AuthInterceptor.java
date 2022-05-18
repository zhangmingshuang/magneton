package org.magneton.support.api.auth.interceptor;

import java.util.Set;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.magneton.core.Response;
import org.magneton.core.ResponseException;
import org.magneton.core.base.Strings;
import org.magneton.support.adapter.InterceptorAdapter;
import org.magneton.support.api.auth.constant.LoginError;
import org.magneton.support.api.auth.properties.ApiAuthProperties;
import org.magneton.support.api.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
@Component
public class AuthInterceptor implements InterceptorAdapter {

	@Autowired
	private ApiAuthProperties apiAuthProperties;

	@Autowired
	private AuthService authService;

	@Override
	public HandlerInterceptorAdapter handlerInterceptorAdapter() {
		return new AuthHandlerInterceptorAdapter();
	}

	@Override
	public Set<String> pathPatterns() {
		return this.merge(this.apiAuthProperties.getAuthInterceptorPathPatterns(), "/api/**");
	}

	@Nullable
	@Override
	public Set<String> excludePathPatterns() {
		Set<String> authInterceptorExcludePathPatterns = this.apiAuthProperties.getAuthInterceptorExcludePathPatterns();
		return this.merge(authInterceptorExcludePathPatterns, "/api/auth/sms/**", "/api/auth/basic/getSecretKey");
	}

	private class AuthHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
			String token = request.getHeader("token");
			String identification = request.getHeader("identification");
			if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(identification)) {
				throw new ResponseException(Response.response(LoginError.HEADER_MISS));
			}
			boolean valid = AuthInterceptor.this.authService.validateToken(token, identification);
			if (!valid) {
				throw new ResponseException(Response.response(LoginError.LOGIN_EXPIRE));
			}
			return true;
		}

	}

}
