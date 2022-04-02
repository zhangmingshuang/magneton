package org.magneton.module.wechat.auth;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.module.core.wechat.WechatAccessToken;

/**
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Slf4j
public class WechatAuthImpl implements WechatAuth {

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%sE&grant_type=authorization_code";

	private static final String URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WechatAutoConfig wechatAutoConfig;

	public WechatAuthImpl(WechatAutoConfig wechatAutoConfig) {
		this.wechatAutoConfig = Preconditions.checkNotNull(wechatAutoConfig);
	}

	@Override
	public Consequences<WechatUserInfo> getUserInfoByCode(WechatUserInfoCodeReq codeReq) {
		Preconditions.checkNotNull(codeReq);
		String code = Preconditions.checkNotNull(codeReq.getCode());
		String requestUrl = String.format(ACCESS_TOKEN_URL, this.wechatAutoConfig.getAppid(),
				this.wechatAutoConfig.getSecret(), code);
		Consequences<WechatAccessToken> response = this.doRequest(requestUrl, WechatAccessToken.class);
		if (!response.isSuccess()) {
			return response.coverage();
		}
		WechatAccessToken wechatAccessToken = response.getData();
		String access_token = wechatAccessToken.getAccess_token();
		String openid = wechatAccessToken.getOpenid();
		return this.getUserInfo(access_token, openid);
	}

	@Override
	public Consequences<WechatUserInfo> getUserInfo(String accessToken, String openid) {
		Preconditions.checkNotNull(accessToken);
		Preconditions.checkNotNull(openid);
		String requestUrl = String.format(URL, accessToken, openid);
		return this.doRequest(requestUrl, WechatUserInfo.class);
	}

	private <T> Consequences<T> doRequest(String requestUrl, Class<T> clazz) {
		// noinspection OverlyBroadCatchBlock
		try {
			String body = HttpUtil.get(requestUrl, 3);
			if (Strings.isNullOrEmpty(body)) {
				return Consequences.failMessageOnly("未知错误，没有响应体数据");
			}
			JsonNode jsonNode = this.objectMapper.readTree(body);
			String errCode = jsonNode.get("errcode").asText();
			if (!Strings.isNullOrEmpty(errCode)) {
				log.error("请求{}错误：{}", requestUrl, body);
				return Consequences.failMessageOnly(jsonNode.get("errmsg").asText());
			}
			T response = this.objectMapper.readValue(jsonNode.traverse(), clazz);
			return Consequences.success(response);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Consequences.failMessageOnly("未知错误");
	}

}
