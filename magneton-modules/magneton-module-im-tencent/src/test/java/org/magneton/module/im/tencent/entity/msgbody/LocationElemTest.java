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
class LocationElemTest {

	@Test
	void test() throws JsonProcessingException {
		LocationElem locationElem = new LocationElem();
		locationElem.setDesc("test");
		locationElem.setLatitude(1);
		locationElem.setLongitude(1);
		String json = TencentImJson.getInstance().writeValueAsString(locationElem.body());
		Assertions.assertEquals(
				"{\"MsgType\":\"location\",\"MsgBody\":{\"Desc\":\"test\",\"Latitude\":1,\"Longitude\":1}}", json);
	}

}