package org.magneton.module.safedog.geetest;

import com.dtflys.forest.Forest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.magneton.module.safedog.BehaviorSafeDog;

/**
 * 极验行为安全狗.
 *
 * https://docs.geetest.com/gt4/apirefer/api/server
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Slf4j
public class GeeTestBehaviorSafeDog implements BehaviorSafeDog<GeeTestBody> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final GeeTestBehaviorConfig config;

	public GeeTestBehaviorSafeDog(GeeTestBehaviorConfig config) {
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

		String url = String.format("http://gcaptcha4.geetest.com/validate?captcha_id=%s", this.config.getCaptchaId());
		String response = Forest.post(url).connectTimeout(Duration.ofSeconds(3)).readTimeout(Duration.ofSeconds(3))
				.onError((e, forestRequest, forestResponse) -> log.error(String.format("request %s error", url), e))
				.addBody(queryParams, MediaType.FORM_DATA.toString()).executeAsString();
		if (log.isDebugEnabled()) {
			log.debug("requestl url {} \n body:{}", url, response);
		}
		if (!Strings.isNullOrEmpty(response)) {
			// noinspection OverlyBroadCatchBlock
			try {
				return "success".equalsIgnoreCase(this.objectMapper.readTree(response).get("result").asText());
			}
			catch (Throwable e) {
				if (log.isDebugEnabled()) {
					log.debug("response not a json", e);
				}
			}
		}
		return false;
	}

}
