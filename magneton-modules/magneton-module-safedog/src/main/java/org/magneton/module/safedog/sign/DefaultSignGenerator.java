package org.magneton.module.safedog.sign;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 签名值生成器.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
@Slf4j
public class DefaultSignGenerator implements SignGenerator {

	@Override
	public String generate(Map<String, String> data, List<String> keys, String salt) {
		StringBuilder builder = new StringBuilder(256);
		String key;
		String value;
		for (int i = 0, s = keys.size(); i < s; i++) {
			key = keys.get(i);
			value = data.get(keys.get(i));
			if (!Strings.isNullOrEmpty(value)) {
				builder.append(key).append('=').append(value).append(",");
			}
		}
		String embeddingStr = builder.substring(0, builder.length() - 1);
		if (!Strings.isNullOrEmpty(salt)) {
			embeddingStr += salt;
		}
		if (log.isDebugEnabled()) {
			log.debug("sign str: {}", embeddingStr);
		}
		// noinspection UnstableApiUsage
		return Hashing.sha256().hashString(embeddingStr, StandardCharsets.UTF_8).toString();
	}

}
