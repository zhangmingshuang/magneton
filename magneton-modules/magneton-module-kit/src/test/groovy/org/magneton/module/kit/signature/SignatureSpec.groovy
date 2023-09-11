package org.magneton.module.kit.signature

import com.google.common.collect.Maps
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
class SignatureSpec {

    @Test
    void build() throws SignatureBodyException {
        Signature signature = Signature.builder().salt("test").needBodyKeys(new String[]{"a", "b"})
                .signatureBodyBuilder((body, salt) -> "body").build();
        Sha1Signature signature1 = new Sha1Signature("test");
        signature1.setSignatureContentBuilder((body, salt) -> "body");
        signature1.setSignatureBodyVerifyer(new KeysSignatureBodyVerifyer(new String[]{"a", "b"}));

        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");
        body.put("b", "bv");
        body.put("c", "");

        String s1 = signature.sign(body);
        String s2 = signature1.sign(body);
        Assertions.assertEquals(s1, s2, "sha1 signature builder has error");
    }

}
