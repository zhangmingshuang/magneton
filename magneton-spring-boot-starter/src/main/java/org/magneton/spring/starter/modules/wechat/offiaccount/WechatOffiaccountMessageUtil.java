package org.magneton.spring.starter.modules.wechat.offiaccount;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.magneton.module.wechat.offiaccount.pojo.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 微信公众号消息工具.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class WechatOffiaccountMessageUtil {

	private static final Logger log = LoggerFactory.getLogger(WechatOffiaccountMessageUtil.class);

	private WechatOffiaccountMessageUtil() {
		// ignore
	}

	/**
	 * 获取消息体.
	 * @param request HttpServletRequest
	 * @return MessageBody
	 */
	public static MessageBody getMessageBody(HttpServletRequest request) throws IOException {
		String msgSignature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String echostr = request.getParameter("echostr");

		String encryptType = request.getParameter("encrypt_type");
		encryptType = StringUtils.isBlank(encryptType) ? "raw" : encryptType;

		MessageBody messageBody = new MessageBody();
		messageBody.setSignature(msgSignature);
		messageBody.setNonce(nonce);
		messageBody.setTimestamp(timestamp);
		messageBody.setEchostr(echostr);
		messageBody.setEncryptType(encryptType);

		// 解析request.inputStream
		ServletInputStream inputStream = request.getInputStream();
		int contentLength = request.getContentLength();
		if (contentLength <= 0) {
			return messageBody;
		}
		try {
			byte[] bytes = new byte[request.getContentLength()];
			// noinspection ResultOfMethodCallIgnored
			inputStream.read(bytes);
			messageBody.setRequestBody(new String(bytes, StandardCharsets.UTF_8));
		}
		catch (IOException e) {
			log.warn("read request input stream error", e);
			messageBody.setRequestBody(null);
		}
		return messageBody;
	}

	@SneakyThrows
	public static void writeResponse(HttpServletResponse response, String content) {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(content);
	}

}