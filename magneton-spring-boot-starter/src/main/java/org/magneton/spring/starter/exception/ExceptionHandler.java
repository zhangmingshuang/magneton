package org.magneton.spring.starter.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.net.MediaType;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.magneton.core.Response;
import org.magneton.core.ResponseMessage;
import org.magneton.foundation.exception.ProcessException;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
public class ExceptionHandler {

	private ExceptionHandler() {
	}

	@CanIgnoreReturnValue
	public static boolean printWithFail(HttpServletResponse httpServletResponse, Response response) {
		Preconditions.checkNotNull(httpServletResponse);
		Preconditions.checkNotNull(response);
		httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		httpServletResponse.setContentType(MediaType.JSON_UTF_8.toString());
		try (PrintWriter writer = httpServletResponse.getWriter()) {
			writer.print(JSON.toJSONString(response));
		}
		catch (IOException e) {
			throw new ProcessException(e);
		}
		return false;
	}

	public static boolean printWithFail(HttpServletResponse httpServletResponse, ResponseMessage responseMessage) {
		printWithFail(httpServletResponse, Response.response(responseMessage));
		return false;
	}

}
