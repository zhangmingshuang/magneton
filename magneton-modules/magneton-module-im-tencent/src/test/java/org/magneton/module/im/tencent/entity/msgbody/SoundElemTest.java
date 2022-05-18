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
class SoundElemTest {

	@Test
	void test() throws JsonProcessingException {
		SoundElem soundElem = new SoundElem();
		soundElem.setUrl("url");
		soundElem.setUuid("uuid");
		soundElem.setSize(0);
		soundElem.setSecond(0);
		soundElem.setDownloadFlag(0);
		String json = TencentImJson.getInstance().writeValueAsString(soundElem.body());
		System.out.println(json);
		Assertions.assertEquals(
				"{\"MsgType\":\"TIMSoundElem\",\"MsgContent\":{\"Url\":\"url\",\"UUID\":\"uuid\",\"Size\":0,\"Second\":0,\"Download_Flag\":0}}",
				json);
	}

}