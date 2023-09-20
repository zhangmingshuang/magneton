package org.magneton.module.safedog.sign;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Result;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 签名安全狗.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
@Slf4j
@AllArgsConstructor
public class SignerImpl implements Signer {

	/**
	 * Key排序器
	 */
	private SignKeySorter signKeySorter;

	/**
	 * 签名生成器
	 */
	private SignGenerator signGenerator;

	/**
	 * 签名缓存
	 */
	private SignValidator signValidator;

	/**
	 * 签名有效期，单位秒.
	 * @apiNote 在签名的有效期内，相同的签名会被认为是错误的。如果设置的值小于 {@code 1}，则不会处理该逻辑。
	 */
	private int signPeriod = -1;

	/**
	 * 必须的key
	 */
	String[] requiredKeys;

	@Override
	public String sign(Map<String, String> data, @Nullable String salt) {
		Preconditions.checkNotNull(data, "data is null");

		if (log.isDebugEnabled()) {
			log.debug("sign data: {}", data);
		}
		Set<String> keys = data.keySet();
		if (this.requiredKeys != null && this.requiredKeys.length > 0) {
			if (keys.isEmpty()) {
				throw new IllegalArgumentException(
						String.format("required key [%s] is empty", Arrays.toString(this.requiredKeys)));
			}
			else {
				for (String requiredKey : this.requiredKeys) {
					String keyValue = data.get(requiredKey);
					if (Strings.isNullOrEmpty(keyValue)) {
						throw new IllegalArgumentException(
								String.format("required key [%s] is not exist or empty", requiredKey));
					}
				}
			}
		}
		List<String> sortedKeys = this.signKeySorter.sort(keys);
		return this.signGenerator.generate(data, sortedKeys, salt);
	}

	@Override
	public Result<Boolean> validate(String sign, Map<String, String> data, @Nullable String salt) {
		Preconditions.checkNotNull(sign, "sign is null");
		Preconditions.checkNotNull(data, "data is null");

		String trulySign = this.sign(data, salt);

		return this.signValidator.validate(trulySign, sign, this.signPeriod);
	}

}
