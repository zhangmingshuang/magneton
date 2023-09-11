package org.magneton.module.kit.signature

import com.google.common.collect.Maps
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import spock.lang.Specification

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/29
 * @see Sha1Signature
 */
class Sha1SignatureSpec extends Specification {

    @Test
    void generateSignature() {
        Sha1Signature test = new Sha1Signature("test");
        String signature = test.generateSignature("test");
        Assertions.assertNotNull(signature, "signature is null");
    }

    @Test
    void getSalt() {
        Sha1Signature test = new Sha1Signature("test");
        Assertions.assertEquals("test", test.getSalt(), "signature salt does not equals");
    }

    @Test
    void sign() throws SignatureBodyException {
        Sha1Signature sha1Signature = new Sha1Signature("test");
        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");
        String signature = sha1Signature.sign(body);
        String signature1 = sha1Signature.generateSignature(sha1Signature.parseSignContent(body));
        Assertions.assertEquals(signature, signature1, "signature generate error");
    }

    @Test
    void parseSignContent() throws SignatureBodyException {
        Sha1Signature sha1Signature = new Sha1Signature("test");
        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");
        String signContext = sha1Signature.parseSignContent(body);
        Assertions.assertNotNull(signContext, "sign content error");
    }

    @Test
    void setSignatureBodyBuilder() throws SignatureBodyException {
        Sha1Signature sha1Signature = new Sha1Signature("test");
        sha1Signature.setSignatureContentBuilder((body, salt) -> "body");
        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");
        String bodyContext = sha1Signature.parseSignContent(body);
        Assertions.assertEquals("body", bodyContext, "signature body builder error");
    }

    @Test
    void getSignatureBodyBuilder() throws SignatureBodyException {
        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");

        Sha1Signature sha1Signature = new Sha1Signature("test");
        SignatureContentBuilder signatureContentBuilder = sha1Signature.getSignatureContentBuilder();
        String signContent = sha1Signature.parseSignContent(body);
        String signContent1 = signatureContentBuilder.build(body, "test");
        Assertions.assertEquals(signContent, signContent1, "sign content generate error");
    }

    @Test
    void getSignatureBodyVerifyer() {
        Sha1Signature sha1Signature = new Sha1Signature("test");
        Assertions.assertNull(sha1Signature.getSignatureBodyVerifyer(), "signature body verifyer default must be null");
        KeysSignatureBodyVerifyer keysSignatureBodyVerifyer = new KeysSignatureBodyVerifyer(new String[]{"a", "b"});
        sha1Signature.setSignatureBodyVerifyer(keysSignatureBodyVerifyer);
        Assertions.assertEquals(keysSignatureBodyVerifyer, sha1Signature.getSignatureBodyVerifyer(),
                "signature body verifyer set and get does not match");
    }

    @Test
    void setSignatureBodyVerifyer() {
        HashMap<String, String> body = Maps.newHashMap();
        body.put("a", "av");

        Sha1Signature sha1Signature = new Sha1Signature("test");
        sha1Signature.setSignatureBodyVerifyer(new KeysSignatureBodyVerifyer(new String[]{"a", "b"}));
        Assertions.assertThrows(SignatureBodyException.class, () -> {
            String sign = sha1Signature.sign(body);
            Assertions.fail("this step must be not touch");
        });
    }

}
