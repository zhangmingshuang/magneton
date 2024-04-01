package org.magneton.module.safedog.geetest;

import com.dtflys.forest.Forest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.magneton.core.Result;
import org.magneton.foundation.constant.Constant;
import org.magneton.module.safedog.BehaviorValidator;
import org.magneton.module.safedog.SafeDogErr;

import java.time.Duration;
import java.util.Map;

/**
 * 极验行为校验器.
 * <p>
 * <a href="https://docs.geetest.com/gt4/apirefer/api/server">官方文档</a>
 * <p>
 * 当用户在前端界面通过验证码后，会产生一批与验证码相关的参数，用户的业务请求带上这些参数，后台业务接口再将这些参数上传到极验二次校验接口，确认该用户本次验证的有效性。
 *
 * @author zhangmsh
 * @since 2.0.8
 */
@Slf4j
public class GeeTestBehaviorValidator implements BehaviorValidator<GeeTestBody> {

	/**
	 * the geetest uri.
	 */
	@SuppressWarnings({ "SpellCheckingInspection", "HttpUrlsUsage" })
	private static final String GEETEST_URI = "http://gcaptcha4.geetest.com/validate?captcha_id=%s";

	/**
	 * 在响应数据中的失败原因字段名称
	 */
	private static final String REASON = "reason";

	/**
	 * 在响应数据中的异常原因字段名称
	 */
	private static final String MSG = "msg";

	/**
	 * json processor
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * configs
	 */
	private final GeeTestBehaviorConfig config;

	public GeeTestBehaviorValidator(GeeTestBehaviorConfig config) {
		this.config = Preconditions.checkNotNull(config, "config must not be null");
		this.config.validate();
	}

	@Override
	public Result<Void> validate(GeeTestBody body) {
		Preconditions.checkNotNull(body, "body must not be null");

		Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(5);
		// 验证流水号
		String lotNumber = Preconditions.checkNotNull(body.getLotNumber(), "lotNumber must not be null");
		queryParams.put("lot_number", lotNumber);
		// 验证输出信息
		String captchaOutput = Preconditions.checkNotNull(body.getCaptchaOutput(), "captchaOutput must not be null");
		queryParams.put("captcha_output", captchaOutput);
		// 验证通过标识
		queryParams.put("pass_token", Preconditions.checkNotNull(body.getPassToken()));
		// 验证通过时间戳
		queryParams.put("gen_time", Preconditions.checkNotNull(body.getGenTime()));
		// 验证签名
		String captchaKey = Preconditions.checkNotNull(this.config.getCaptchaKey(), "captchaKey must not be null");
		String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);
		queryParams.put("sign_token", signToken);

		String url = String.format(GEETEST_URI, this.config.getCaptchaId());

		// noinspection UnstableApiUsage
		String response = Forest.post(url)
				// timeout config
				.connectTimeout(Duration.ofMillis(this.config.getConnectTimeout()))
				.readTimeout(Duration.ofMillis(this.config.getReadTimeout()))
				// error process
				.onError((e, forestRequest, forestResponse) -> log.error(String.format("request %s error", url), e))
				.addBody(queryParams, MediaType.FORM_DATA.toString()).executeAsString();

		if (log.isDebugEnabled()) {
			log.debug("request url {} \n body:{}", url, response);
		}

		if (Strings.isNullOrEmpty(response)) {
			return Result.fail(SafeDogErr.GEETEST_RESPONSE_EMPTY);
		}

		// noinspection OverlyBroadCatchBlock
		try {
			// result string 二次校验结果
			// reason string 校验结果说明
			// captcha_args dict 验证输出参数
			JsonNode jsonResult = this.objectMapper.readTree(response);
			String result = jsonResult.get(Constant.RESULT).asText();
			boolean success = Constant.SUCCESS.equalsIgnoreCase(result);
			if (success) {
				// 校验成功返回示例
				// {
				// "result": "success", "reason": "", "captcha_args": {
				// "used_type": "slide", "user_ip": "127.0.0.1",
				// "lot_number": "4dc3cfc2cdff448cad8d13107198d473",
				// "scene": "反爬虫",
				// "referer": "http://127.0.0.1:8077/"
				// }
				// }
				return Result.success();
			}
			// // 校验失败返回示例
			// {
			// "result": "fail", "reason": "pass_token expire", "captcha_args": {
			// "used_type": "slide",
			// "user_ip": "127.0.0.1",
			// "lot_number": "4dc3cfc2cdff448cad8d13107198d473",
			// "scene": "反爬虫",
			// "referer": "http://127.0.0.1:8077/"
			// }
			// }
			if (Constant.FAIL.equals(result)) {
				String reason = jsonResult.get(REASON).asText();
				return Result.failBy(reason);
			}
			//
			//// 请求异常返回示例
			// {
			// "status": "error", "code": "-50005", "msg": "illegal gen_time",
			// "desc": {
			// "type": "defined error"
			// }
			// }
			if (Constant.ERROR.equals(result)) {
				String msg = jsonResult.get(MSG).asText();
				return Result.failBy(msg);
			}

			return Result.fail(SafeDogErr.GEETEST_RESPONSE_ERROR);
		}
		catch (Throwable e) {
			throw new IllegalArgumentException(e);
		}
	}

}