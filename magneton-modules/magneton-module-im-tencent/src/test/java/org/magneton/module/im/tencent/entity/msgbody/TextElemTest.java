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
class TextElemTest {

	@Test
	void test() throws JsonProcessingException {
		TextElem textElem = new TextElem();
		textElem.setText("test");
		String json = TencentImJson.getInstance().writeValueAsString(textElem.body());
		Assertions.assertEquals("{\"MsgType\":\"TIMTextElem\",\"MsgContent\":{\"Text\":\"test\"}}", json);
	}

}