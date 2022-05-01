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
class CustomElemTest {

	@Test
	void test() throws JsonProcessingException {
		CustomElem customElem = new CustomElem();
		customElem.setData("a");
		customElem.setDesc("d");
		customElem.setExt("e");
		customElem.setSound("s");
		String json = TencentImJson.getInstance().writeValueAsString(customElem.body());
		Assertions.assertEquals(
				"{\"MsgType\":\"TIMCustomElem\",\"MsgContent\":{\"Data\":\"a\",\"Desc\":\"d\",\"Ext\":\"e\",\"Sound\":\"s\"}}",
				json);
	}

}