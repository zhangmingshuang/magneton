package org.magneton.module.safedog.sign;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundation.MoreCollections;

import javax.annotation.Nullable;
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

	public static final String DEFAULT_LINK_SYMBOL = ",";

	private final String linkSymbol;

	public DefaultSignGenerator() {
		this(DEFAULT_LINK_SYMBOL);
	}

	public DefaultSignGenerator(String linkSymbol) {
		this.linkSymbol = linkSymbol;
	}

	@Override
	public String generate(Map<String, String> data, @Nullable List<String> keys, @Nullable String salt) {
		String embeddingStr = this.getSignString(data, keys, salt);
		// noinspection UnstableApiUsage
		return Hashing.sha256().hashString(embeddingStr, StandardCharsets.UTF_8).toString();
	}

	@Override
	public String getSignString(Map<String, String> data, @Nullable List<String> keys, @Nullable String salt) {
		StringBuilder builder = new StringBuilder(256);
		String key;
		String value;
		if (MoreCollections.isNullOrEmpty(keys)) {
			keys = Lists.newArrayList(data.keySet());
		}
		for (String string : keys) {
			key = string;
			value = data.get(string);
			if (!Strings.isNullOrEmpty(value)) {
				builder.append(key).append('=').append(value).append(this.linkSymbol);
			}
		}
		String embeddingStr = builder.substring(0, builder.length() - this.linkSymbol.length());
		if (!Strings.isNullOrEmpty(salt)) {
			embeddingStr += this.linkSymbol + salt;
		}
		if (log.isDebugEnabled()) {
			log.debug("sign str: {}", embeddingStr);
		}
		return embeddingStr;
	}

}