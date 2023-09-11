package org.magneton.module.wechat.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * WeChat json processor.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class WxJson {

	private static final WxJson INSTANCE = new WxJson();

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		// 只序列化非空字段
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.PROTECTED_AND_PUBLIC);
		// 忽略未知字段
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 忽略Null字段
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
	}

	public static WxJson getInstance() {
		return INSTANCE;
	}

	private WxJson() {
		// private
	}

	public String writeValueAsString(Object object) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(object);
	}

	public <T> T readValue(byte[] value, Class<T> type) throws IOException {
		return OBJECT_MAPPER.readValue(value, type);
	}

	public <T> T readValue(String value, Class<T> type) throws JsonProcessingException {
		return OBJECT_MAPPER.readValue(value, type);
	}

}