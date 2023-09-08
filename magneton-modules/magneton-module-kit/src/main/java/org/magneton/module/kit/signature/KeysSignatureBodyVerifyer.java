package org.magneton.module.kit.signature;

import java.util.Map;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public class KeysSignatureBodyVerifyer implements SignatureBodyVerifyer {

	private final String[] needBodyKeys;

	public KeysSignatureBodyVerifyer(String[] needBodyKeys) {
		this.needBodyKeys = needBodyKeys;
	}

	@Override
	public void validate(Map<String, String> body) throws SignatureBodyException {
		for (String needBodyKey : this.needBodyKeys) {
			if (!body.containsKey(needBodyKey)) {
				throw new SignatureBodyException(needBodyKey + " not founded in body");
			}
		}
	}

}
