package org.magneton.module.wechat.core;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.Strings;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
@Slf4j
public class Req {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private Req() {
	}

	public static <T> Consequences<T> doGet(String requestUrl, Class<T> clazz) {
		// noinspection OverlyBroadCatchBlock
		try {
			String body = HttpUtil.get(requestUrl, 3);
			if (Strings.isNullOrEmpty(body)) {
				return Consequences.failMessageOnly("未知错误，没有响应体数据");
			}
			JsonNode jsonNode = OBJECT_MAPPER.readTree(body);
			String errCode = jsonNode.get("errcode").asText();
			if (!Strings.isNullOrEmpty(errCode)) {
				log.error("请求{}错误：{}", requestUrl, body);
				return Consequences.failMessageOnly(jsonNode.get("errmsg").asText());
			}
			T response = OBJECT_MAPPER.readValue(jsonNode.traverse(), clazz);
			return Consequences.success(response);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Consequences.failMessageOnly("未知错误");
	}

}
