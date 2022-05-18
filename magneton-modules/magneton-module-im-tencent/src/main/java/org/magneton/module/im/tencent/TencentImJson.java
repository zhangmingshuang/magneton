package org.magneton.module.im.tencent;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TencentImJson {

	private static final TencentImJson INSTANCE = new TencentImJson();

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.PROTECTED_AND_PUBLIC);
		// 忽略Null字段
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
	}

	public static TencentImJson getInstance() {
		return INSTANCE;
	}

	private TencentImJson() {
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