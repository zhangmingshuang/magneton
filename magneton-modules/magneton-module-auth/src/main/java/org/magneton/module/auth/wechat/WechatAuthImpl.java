package org.magneton.module.auth.wechat;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;

/**
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Slf4j
public class WechatAuthImpl implements WechatAuth {

	private static final String URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Consequences<WechatUserInfo> getUserInfo(String accessToken, String openid) {
		Preconditions.checkNotNull(accessToken);
		Preconditions.checkNotNull(openid);
		String requestUrl = String.format(URL, accessToken, openid);
		String body = HttpUtil.get(requestUrl, 3);
		if (Strings.isNullOrEmpty(body)) {
			return Consequences.failMessageOnly("未知错误，没有响应体数据");
		}
		// noinspection OverlyBroadCatchBlock
		try {
			JsonNode jsonNode = this.objectMapper.readTree(body);
			String errCode = jsonNode.get("errcode").asText();
			if (!Strings.isNullOrEmpty(errCode)) {
				log.error("请求{}错误：{}", requestUrl, body);
				return Consequences.failMessageOnly(jsonNode.get("errmsg").asText());
			}
			WechatUserInfo wechatUserInfo = this.objectMapper.readValue(jsonNode.traverse(), WechatUserInfo.class);
			return Consequences.success(wechatUserInfo);
		}
		catch (IOException e) {
			log.error("parse body error", e);
		}
		return Consequences.failMessageOnly("未知错误");
	}

}
