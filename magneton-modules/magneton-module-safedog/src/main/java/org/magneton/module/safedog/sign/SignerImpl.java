package org.magneton.module.safedog.sign;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Result;
import org.magneton.module.safedog.SafeDogErr;

import javax.annotation.Nullable;
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
	public Result<String> sign(Map<String, String> data, @Nullable String salt) {
		Preconditions.checkNotNull(data, "data is null");
		if (log.isDebugEnabled()) {
			log.debug("sign data: {}", data);
		}

		Set<String> keys = data.keySet();
		if (this.requiredKeys != null) {
			for (String requiredKey : this.requiredKeys) {
				List<String> missKeys = Lists.newArrayListWithCapacity(this.requiredKeys.length);
				String keyValue = data.get(requiredKey);
				if (Strings.isNullOrEmpty(keyValue)) {
					missKeys.add(requiredKey);
				}
				return Result.fail(SafeDogErr.SIGN_REQUIRE_KEY_MISS.withArgs(missKeys));
			}
		}
		List<String> sortedKeys = this.signKeySorter.sort(keys);
		return Result.successWith(this.signGenerator.generate(data, sortedKeys, salt));
	}

	@Override
	public Result<Void> validate(String sign, Map<String, String> data, @Nullable String salt) {
		Preconditions.checkNotNull(sign, "sign is null");
		Preconditions.checkNotNull(data, "data is null");

		Result<String> trulySign = this.sign(data, salt);
		if (!trulySign.isSuccess()) {
			return trulySign.assignment();
		}
		return this.signValidator.validate(trulySign.getData(), sign, this.signPeriod);
	}

}