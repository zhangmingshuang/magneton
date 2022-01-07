package org.magneton.core.signature;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.Map;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public class DefaultSignatureContentBuilder implements SignatureContentBuilder {

	private static final int DEFAULT_CAPACITY = 64;

	private static final char APPEND = '&';

	private static final char LINK = '=';

	private static final String[] EMPTY_STRINGS = new String[0];

	@Override
	public String build(Map<String, String> body, String salt) {
		if (body.isEmpty()) {
			return salt;
		}
		String[] keys = body.keySet().toArray(EMPTY_STRINGS);
		Arrays.sort(keys);

		StringBuilder builder = new StringBuilder(DEFAULT_CAPACITY);
		for (String key : keys) {
			String value = body.get(key);
			if (Strings.isNullOrEmpty(value)) {
				continue;
			}
			builder.append(key).append(LINK).append(value).append(APPEND);
		}
		builder.append(salt);
		if (builder.length() < 1) {
			return salt;
		}
		return salt.isEmpty() ? builder.substring(0, builder.length() - 1) : builder.toString();
	}

}
