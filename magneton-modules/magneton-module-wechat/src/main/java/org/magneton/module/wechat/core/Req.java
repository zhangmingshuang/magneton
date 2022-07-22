package org.magneton.module.wechat.core;

import java.io.IOException;
import java.util.Map;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;

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
			String body = HttpUtil.get(requestUrl, 3000);
			return doResponse(requestUrl, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Consequences.failMessageOnly("未知错误");
	}

	public static <T> Consequences<T> doPost(String url, Map<String, Object> data, Class<T> clazz) {
		try {
			String body = HttpUtil.post(url, data);
			return doResponse(url, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Consequences.failMessageOnly("未知错误");
	}

	private static <T> Consequences<T> doResponse(String requestUrl, Class<T> clazz, String body) throws IOException {
		if (Strings.isNullOrEmpty(body)) {
			return Consequences.failMessageOnly("未知错误，没有响应体数据");
		}
		JsonNode jsonNode = OBJECT_MAPPER.readTree(body);
		JsonNode errCodeNode = jsonNode.get("errcode");
		if (errCodeNode != null && !Strings.isNullOrEmpty(errCodeNode.asText())) {
			log.error("请求{}错误：{}", requestUrl, body);
			return Consequences.failMessageOnly(jsonNode.get("errmsg").asText());
		}
		if (log.isDebugEnabled()) {
			log.debug("requestUrl:{} , response=[{}]", requestUrl, body);
		}
		T response = OBJECT_MAPPER.readValue(jsonNode.traverse(), clazz);
		return Consequences.success(response);
	}

}
