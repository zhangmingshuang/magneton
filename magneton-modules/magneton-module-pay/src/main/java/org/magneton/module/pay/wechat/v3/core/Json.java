package org.magneton.module.pay.wechat.v3.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Json {

	private static final Json INSTANCE = new Json();

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, Visibility.PROTECTED_AND_PUBLIC);
	}

	public static Json getInstance() {
		return INSTANCE;
	}

	private Json() {
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