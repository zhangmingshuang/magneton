package org.magneton.enhance.safedog;

import org.magneton.enhance.safedog.sign.*;

/**
 * 签名器构造器.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class SignerBuilder {

	/**
	 * Key排序器
	 */
	SignKeySorter signKeySorter;

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

	/**
	 * 设置Key的排序器.
	 * @param signKeySorter Key排序器
	 * @return this
	 */
	public SignerBuilder keySorter(SignKeySorter signKeySorter) {
		this.signKeySorter = signKeySorter;
		return this;
	}

	/**
	 * 设置签名生成器
	 * @param signGenerator 签名生成器
	 * @return this
	 */
	public SignerBuilder signGenerator(SignGenerator signGenerator) {
		this.signGenerator = signGenerator;
		return this;
	}

	/**
	 * 设置签名有效期，单位秒.
	 * @param signPeriod 签名有效期，单位秒
	 * @return this
	 */
	public SignerBuilder signPeriod(int signPeriod) {
		this.signPeriod = signPeriod;
		return this;
	}

	public Signer build() {
		if (this.signKeySorter == null) {
			this.signKeySorter = new DefaultSignKeySorter();
		}
		if (this.signGenerator == null) {
			this.signGenerator = new DefaultSignGenerator();
		}
		if (this.signValidator == null) {
			this.signValidator = new MemorySignValidator();
		}
		return new SignerImpl(this.signKeySorter, this.signGenerator, this.signValidator, this.signPeriod,
				this.requiredKeys);
	}

}