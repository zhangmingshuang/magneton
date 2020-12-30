package org.magneton.core.signature;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Objects;
import org.magneton.core.util.MoreArrays;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 */
public interface Signature {

  /**
   * get signature builder.
   *
   * @return the builder.
   */
  static Builder builder() {
    return new Builder();
  }

  /**
   * do body signature.
   *
   * @param body the given body to signature.
   * @return the signature.
   * @throws SignatureBodyException if body does not in line with expectations.
   */
  String sign(Map<String, String> body) throws SignatureBodyException;

  /**
   * parse the signature body do signature content.
   *
   * @param body the given body to signature.
   * @return the signature content.
   * @throws SignatureBodyException if body does not in line with expectations.
   */
  String parseSignContent(Map<String, String> body) throws SignatureBodyException;

  class Builder {
    private AbstractSignature signature;
    private String salt;
    private String[] needBodyKeys;
    private SignatureBodyBuilder signatureBodyBuilder;

    public Builder signature(AbstractSignature signature) {
      Preconditions.checkNotNull(signature, "signature must be not null");

      this.signature = signature;
      return this;
    }

    public Builder salt(String salt) {
      this.salt = salt;
      return this;
    }

    public Builder needBodyKeys(String[] needBodyKeys) {
      Preconditions.checkNotNull(needBodyKeys, "needBodyKeys must be not null");

      this.needBodyKeys = needBodyKeys;
      return this;
    }

    public Builder signatureBodyBuilder(SignatureBodyBuilder signatureBodyBuilder) {
      Preconditions.checkNotNull(signatureBodyBuilder, "signatureBodyBuilder must be not null");

      this.signatureBodyBuilder = signatureBodyBuilder;
      return this;
    }

    /**
     * build signature.
     *
     * @return the signature.
     */
    public Signature build() {
      Preconditions.checkNotNull(this.salt, "salt must be not null");

      AbstractSignature abstractSignature =
          Objects.isNull(this.signature) ? new Sha1Signature(this.salt) : this.signature;
      if (!MoreArrays.isNullOrEmpty(this.needBodyKeys)) {
        abstractSignature.setSignatureBodyVerifyer(
            new KeysSignatureBodyVerifyer(this.needBodyKeys));
      }
      if (Objects.nonNull(this.signatureBodyBuilder)) {
        abstractSignature.setSignatureBodyBuilder(this.signatureBodyBuilder);
      }
      return abstractSignature;
    }
  }
}
