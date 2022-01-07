package org.magneton.core.signature;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
@Getter
public abstract class AbstractSignature implements Signature {

	private final String salt;

	private SignatureContentBuilder signatureContentBuilder = new DefaultSignatureContentBuilder();

	@Setter
	@Nullable
	private SignatureBodyVerifyer signatureBodyVerifyer;

	protected AbstractSignature(String salt) {
		Preconditions.checkNotNull(salt, "salt must be not null");

		this.salt = salt;
	}

	@Override
	public String sign(Map<String, String> body) throws SignatureBodyException {
		String signatureContent = this.parseSignContent(body);

		return this.generateSignature(signatureContent);
	}

	/**
	 * generate signature.
	 * @param signatureContent the signature content.
	 * @return signature.
	 */
	protected abstract String generateSignature(String signatureContent);

	/**
	 * get generate signature content salt.
	 * @return the signature context salt.
	 */
	protected String getSalt() {
		return this.salt;
	}

	@Override
	public String parseSignContent(Map<String, String> body) throws SignatureBodyException {
		Preconditions.checkNotNull(body, "body must be not null");

		if (Objects.nonNull(this.signatureBodyVerifyer)) {
			this.signatureBodyVerifyer.validate(body);
		}
		return this.signatureContentBuilder.build(body, this.getSalt());
	}

	/**
	 * set signurate body builder.
	 * @param signatureContentBuilder the signature body builder.
	 */
	public void setSignatureContentBuilder(SignatureContentBuilder signatureContentBuilder) {
		Preconditions.checkNotNull(signatureContentBuilder, "signatureBodyBuilder must be not null");

		this.signatureContentBuilder = signatureContentBuilder;
	}

}
