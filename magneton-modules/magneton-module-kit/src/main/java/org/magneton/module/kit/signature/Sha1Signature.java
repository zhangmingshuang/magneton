package org.magneton.module.kit.signature;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public class Sha1Signature extends AbstractSignature {

	/**
	 * sha signature.
	 * @param salt signature content salt
	 */
	public Sha1Signature(String salt) {
		super(salt);
	}

	@Override
	protected String generateSignature(String signatureContent) {
		return Hashing.sha1().hashString(signatureContent, StandardCharsets.UTF_8).toString();
	}

}
