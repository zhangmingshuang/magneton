package org.magneton.spring.starter.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.magneton.core.io.ByteStreams;

/**
 * @author zhangmsh 2022/3/29
 * @since 1.0.0
 */
public class CachedHttpRequestWrapper extends HttpServletRequestWrapper {

	private static final byte[] EMPTY_REQUEST_BODY = new byte[0];

	private byte[] requestBody;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public CachedHttpRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			this.requestBody = ByteStreams.toByteArray(request.getInputStream());
		}
		catch (IOException e) {
			this.requestBody = EMPTY_REQUEST_BODY;
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		System.out.println("------------------get input stream -------------");
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
