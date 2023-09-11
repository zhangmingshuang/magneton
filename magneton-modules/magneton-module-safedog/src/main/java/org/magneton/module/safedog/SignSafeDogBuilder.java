package org.magneton.module.safedog;

import org.magneton.module.safedog.sign.*;

/**
 * @author zhangmsh.
 * @since 2023.9
 */
public class SignSafeDogBuilder {

	/**
	 * Key排序器
	 */
	KeySorter keySorter;

	/**
	 * 签名生成器
	 */
	SignGenerator signGenerator;

	/**
	 * 签名缓存
	 */
	SignValidator signValidator;

	/**
	 * 签名有效期，单位秒.
	 * @apiNote 在签名的有效期内，相同的签名会被认为是错误的。如果设置的值小于 {@code 1}，则不会处理该逻辑。
	 */
	int signPeriod = -1;

	/**
	 * 必须的key
	 */
	String[] requiredKeys;

	public SignSafeDogBuilder keySorter(KeySorter keySorter) {
		this.keySorter = keySorter;
		return this;
	}

	public SignSafeDogBuilder signGenerator(SignGenerator signGenerator) {
		this.signGenerator = signGenerator;
		return this;
	}

	public SignSafeDogBuilder signPeriod(int signPeriod) {
		this.signPeriod = signPeriod;
		return this;
	}

	public SignSafeDog build() {
		if (this.keySorter == null) {
			this.keySorter = new DefaultKeySorter();
		}
		if (this.signGenerator == null) {
			this.signGenerator = new DefaultSignGenerator();
		}
		if (this.signValidator == null) {
			this.signValidator = new MemorySignValidator();
		}
		return new SignSafeDogImpl(this.keySorter, this.signGenerator, this.signValidator, this.signPeriod,
                this.requiredKeys);
	}

}
