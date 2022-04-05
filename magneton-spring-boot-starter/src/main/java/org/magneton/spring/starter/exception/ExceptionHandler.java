package org.magneton.spring.starter.exception;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.magneton.core.Response;
import org.magneton.core.ResponseMessage;
import org.magneton.core.base.Preconditions;
import org.magneton.core.net.MediaType;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
public class ExceptionHandler {

	private ExceptionHandler() {
	}

	public static boolean printWithFail(HttpServletResponse response, ResponseMessage responseMessage) {
		print(response, responseMessage);
		return false;
	}

	public static void print(HttpServletResponse response, ResponseMessage responseMessage) {
		Preconditions.checkNotNull(response);
		Preconditions.checkNotNull(responseMessage);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		response.setContentType(MediaType.JSON_UTF_8.toString());
		try (PrintWriter writer = response.getWriter()) {
			writer.print(JSON.toJSONString(Response.response(responseMessage)));
		}
		catch (IOException e) {
			throw new ProcessException(e);
		}
	}

}
