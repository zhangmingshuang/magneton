package org.magneton.spring.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.net.MediaType;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.magneton.core.Result;
import org.magneton.core.ResultBody;
import org.magneton.foundation.exception.ProcessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
public class ExceptionHandler {

	private ExceptionHandler() {
	}

	@CanIgnoreReturnValue
	public static boolean printWithFail(HttpServletResponse httpServletResponse, Result result) {
		Preconditions.checkNotNull(httpServletResponse);
		Preconditions.checkNotNull(result);
		httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		httpServletResponse.setContentType(MediaType.JSON_UTF_8.toString());
		try (PrintWriter writer = httpServletResponse.getWriter()) {
			writer.print(JSON.toJSONString(result));
		}
		catch (IOException e) {
			throw new ProcessException(e);
		}
		return false;
	}

	public static boolean printWithFail(HttpServletResponse httpServletResponse, ResultBody resultBody) {
		printWithFail(httpServletResponse, Result.valueOf(resultBody));
		return false;
	}

}
