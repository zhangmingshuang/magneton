package org.magneton.module.pay.wechat.v3.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class WxPayJson {

	private static final WxPayJson INSTANCE = new WxPayJson();

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.PROTECTED_AND_PUBLIC);
		// 忽略Null字段
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
	}

	public static WxPayJson getInstance() {
		return INSTANCE;
	}

	private WxPayJson() {
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