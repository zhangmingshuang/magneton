package org.magneton.module.safedog.geetest;

import com.dtflys.forest.Forest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.magneton.foundation.constant.Constant;
import org.magneton.module.safedog.BehaviorValidator;

import java.time.Duration;
import java.util.Map;

/**
 * 极验行为校验器.
 * <p>
 * <a href="https://docs.geetest.com/gt4/apirefer/api/server">官方文档</a>
 *
 * @author zhangmsh
 * @since 2.0.8
 */
@Slf4j
public class GeeTestBehaviorValidator implements BehaviorValidator<GeeTestBody> {

	/**
	 * the geetest uri.
	 */
	public static final String GEETEST_URI = "http://gcaptcha4.geetest.com/validate?captcha_id=%s";

	/**
	 * json processor
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * configs
	 */
	private final GeeTestBehaviorConfig config;

	public GeeTestBehaviorValidator(GeeTestBehaviorConfig config) {
		this.config = Preconditions.checkNotNull(config);
		this.config.validate();
	}

	@Override
	public boolean validate(GeeTestBody body) {
		String captchaKey = this.config.getCaptchaKey();
		String lotNumber = Preconditions.checkNotNull(body.getLotNumber());

		String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);

		Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(5);
		queryParams.put("lot_number", lotNumber);
		queryParams.put("captcha_output", Preconditions.checkNotNull(body.getCaptchaOutput()));
		queryParams.put("pass_token", Preconditions.checkNotNull(body.getPassToken()));
		queryParams.put("gen_time", Preconditions.checkNotNull(body.getGenTime()));
		queryParams.put("sign_token", signToken);

		String url = String.format(GEETEST_URI, this.config.getCaptchaId());
		String response = Forest.post(url).connectTimeout(Duration.ofSeconds(3)).readTimeout(Duration.ofSeconds(3))
				.onError((e, forestRequest, forestResponse) -> log.error(String.format("request %s error", url), e))
				.addBody(queryParams, MediaType.FORM_DATA.toString()).executeAsString();

		if (log.isDebugEnabled()) {
			log.debug("request url {} \n body:{}", url, response);
		}
		if (!Strings.isNullOrEmpty(response)) {
			// noinspection OverlyBroadCatchBlock
			try {
				String result = this.objectMapper.readTree(response).get(Constant.RESULT).asText();
				return Constant.SUCCESS.equalsIgnoreCase(result);
			}
			catch (Throwable e) {
				throw new IllegalArgumentException(e);
			}
		}
		return false;
	}

}
