package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.im.tencent.TencentImJson;

/**
 * .
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
class FaceElemTest {

	@Test
	void test() throws JsonProcessingException {
		FaceElem faceElem = new FaceElem();
		faceElem.setIndex(1);
		faceElem.setData("aa");
		String json = TencentImJson.getInstance().writeValueAsString(faceElem.body());
		System.out.println(json);
		Assertions.assertEquals("{\"index\":1,\"data\":\"aa\"}", json);
	}

}