package org.magneton.module.safedog.sign;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.core.collect.Lists;
import org.magneton.core.hash.Hashing;
import org.magneton.module.safedog.SignSafeDog;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractSignSafeDog implements SignSafeDog {

	private static boolean debug = Boolean.parseBoolean(System.getProperty("module.safedog.sign", "false"));

	@Override
	public String sign(Map<String, String> data, String salt) {
		Preconditions.checkNotNull(data);
		List<String> keys = this.keysAmend(data.keySet());
		if (keys.isEmpty()) {
			return "";
		}
		return this.generateSign(data, keys, salt);
	}

	protected String generateSign(Map<String, String> data, List<String> keys, String salt) {
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
		if (debug) {
			log.info("debug sign str: {}", embeddingStr);
		}
		return Hashing.sha256().hashString(embeddingStr, StandardCharsets.UTF_8).toString();
	}

	protected List<String> keysAmend(Set<String> keySet) {
		if (keySet.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> keys = Lists.newArrayList(keySet);
		Collections.sort(keys);
		if (debug) {
			AtomicInteger i = new AtomicInteger(1);
			for (String key : keys) {
				log.info("sorted key : {}.{}", i.getAndIncrement(), key);
			}
		}
		return keys;
	}

}
