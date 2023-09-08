package org.magneton.module.wechat.core;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
@Slf4j
public class Req {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private Req() {
	}

	public static <T> Reply<T> doGet(String requestUrl, Class<T> clazz) {
		// noinspection OverlyBroadCatchBlock
		try {
			String body = HttpUtil.get(requestUrl, 3);
			return doResponse(requestUrl, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Reply.failMsg("未知错误");
	}

	public static <T> Reply<T> doPost(String url, Map<String, Object> data, Class<T> clazz) {
		try {
			String body = HttpUtil.post(url, data);
			return doResponse(url, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Reply.failMsg("未知错误");
	}

	private static <T> Reply<T> doResponse(String requestUrl, Class<T> clazz, String body) throws IOException {
		if (Strings.isNullOrEmpty(body)) {
			return Reply.failMsg("未知错误，没有响应体数据");
		}
		JsonNode jsonNode = OBJECT_MAPPER.readTree(body);
		String errCode = jsonNode.get("errcode").asText();
		if (!Strings.isNullOrEmpty(errCode)) {
			log.error("请求{}错误：{}", requestUrl, body);
			return Reply.failMsg(jsonNode.get("errmsg").asText());
		}
		T response = OBJECT_MAPPER.readValue(jsonNode.traverse(), clazz);
		return Reply.success(response);
	}

}
