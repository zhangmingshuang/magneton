package org.magneton.module.wechat.core;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Result;

import java.io.IOException;
import java.util.Map;

/**
 * Wechat request Utils.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@Slf4j
public class WxReq {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private WxReq() {
	}

	public static <T> Result<T> doGet(String requestUrl, Class<T> clazz) {
		// noinspection OverlyBroadCatchBlock
		try {
			String body = HttpUtil.get(requestUrl, 3);
			return doResponse(requestUrl, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Result.failBy("未知错误");
	}

	public static <T> Result<T> doPost(String url, Map<String, Object> data, Class<T> clazz) {
		try {
			String body = HttpUtil.post(url, data);
			return doResponse(url, clazz, body);
		}
		catch (Throwable e) {
			log.error("parse body error", e);
		}
		return Result.failBy("未知错误");
	}

	private static <T> Result<T> doResponse(String requestUrl, Class<T> clazz, String body) throws IOException {
		if (Strings.isNullOrEmpty(body)) {
			return Result.failBy("未知错误，没有响应体数据");
		}
		JsonNode jsonNode = OBJECT_MAPPER.readTree(body);
		String errCode = jsonNode.get("errcode").asText();
		if (!Strings.isNullOrEmpty(errCode)) {
			log.error("请求{}错误：{}", requestUrl, body);
			return Result.failBy(jsonNode.get("errmsg").asText());
		}
		T response = OBJECT_MAPPER.readValue(jsonNode.traverse(), clazz);
		return Result.successWith(response);
	}

}
