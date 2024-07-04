package org.magneton.spring.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author zhangmsh 2022/3/29
 * @since 1.0.0
 */
public class CachedHttpRequestWrapper extends HttpServletRequestWrapper {

	private static final byte[] EMPTY_REQUEST_BODY = new byte[0];

	private byte[] requestBody;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private JsonNode requestBodyNode = null;

	public CachedHttpRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			this.requestBody = ByteStreams.toByteArray(request.getInputStream());
			this.requestBodyNode = this.objectMapper.readTree(this.requestBody);
		}
		catch (IOException e) {
			this.requestBody = EMPTY_REQUEST_BODY;
		}
	}

	@Override
	public String getParameter(String name) {
		if (!Strings.isNullOrEmpty(name) && this.hasBody()) {
			JsonNode jsonNode = this.requestBodyNode.get(name);
			return jsonNode == null ? null : jsonNode.asText();
		}
		return super.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (this.hasBody()) {
			return Collections.enumeration(Lists.newArrayList(this.requestBodyNode.fieldNames()));
		}
		return super.getParameterNames();
	}

	public byte[] getRequestBody() {
		return this.requestBody;
	}

	private boolean hasBody() {
		return this.requestBodyNode != null && this.requestBody != null && this.requestBody.length > 0;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.requestBody);
		// noinspection OverlyComplexAnonymousInnerClass
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
	}

}