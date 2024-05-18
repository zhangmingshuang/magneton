package org.magneton.spring.starter.modules.wechat.offiaccount;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.magneton.module.wechat.mp.core.message.pojo.MpMsgBody;
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
public class WechatMpUtil {

	private static final Logger log = LoggerFactory.getLogger(WechatMpUtil.class);

	private WechatMpUtil() {
		// ignore
	}

	/**
	 * 获取消息体.
	 * @param request HttpServletRequest
	 * @return MessageBody
	 */
	public static MpMsgBody parseMsgBody(HttpServletRequest request) throws IOException {
		String msgSignature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String echostr = request.getParameter("echostr");

		String encryptType = request.getParameter("encrypt_type");
		encryptType = StringUtils.isBlank(encryptType) ? "raw" : encryptType;

		MpMsgBody mpMsgBody = new MpMsgBody();
		mpMsgBody.setSignature(msgSignature);
		mpMsgBody.setNonce(nonce);
		mpMsgBody.setTimestamp(timestamp);
		mpMsgBody.setEchostr(echostr);
		mpMsgBody.setEncryptType(encryptType);

		// 解析request.inputStream
		ServletInputStream inputStream = request.getInputStream();
		int contentLength = request.getContentLength();
		if (contentLength <= 0) {
			return mpMsgBody;
		}
		try {
			byte[] bytes = new byte[request.getContentLength()];
			// noinspection ResultOfMethodCallIgnored
			inputStream.read(bytes);
			mpMsgBody.setRequestBody(new String(bytes, StandardCharsets.UTF_8));
		}
		catch (IOException e) {
			log.warn("read request input stream error", e);
			mpMsgBody.setRequestBody(null);
		}
		return mpMsgBody;
	}

	@SneakyThrows
	public static void writeTo(HttpServletResponse response, String content) {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(content);
	}

}