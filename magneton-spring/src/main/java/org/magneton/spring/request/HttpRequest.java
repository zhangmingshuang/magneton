package org.magneton.spring.request;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.magneton.spring.core.exception.ProcessException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public class HttpRequest {

	private HttpRequest() {
	}

	public static String getBody(HttpServletRequest httpServletRequest) {
		Preconditions.checkNotNull(httpServletRequest);
		StringBuilder bodyBuilder = new StringBuilder(1024);
		String line;
		try (ServletInputStream inputStream = httpServletRequest.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			// noinspection NestedAssignment
			while ((line = reader.readLine()) != null) {
				bodyBuilder.append(line);
			}
		}
		catch (IOException e) {
			throw new ProcessException(e);
		}
		return bodyBuilder.toString();
	}

	/**
	 * 获取请求头.
	 * @param httpServletRequest httpServletRequest
	 * @return 请求头
	 */
	public static Map<String, String> getHeaders(HttpServletRequest httpServletRequest) {
		Preconditions.checkNotNull(httpServletRequest);
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		Map<String, String> headers = Maps.newHashMapWithExpectedSize(4);
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = httpServletRequest.getHeader(headerName);
			if (!Strings.isNullOrEmpty(headerValue)) {
				headers.put(headerName, headerValue);
			}
		}
		return headers;
	}

}
