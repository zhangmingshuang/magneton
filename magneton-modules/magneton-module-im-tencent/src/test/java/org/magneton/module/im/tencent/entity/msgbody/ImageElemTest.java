package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.im.tencent.TencentImJson;

/**
 * .
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
class ImageElemTest {

	@Test
	void test() throws JsonProcessingException {
		ImageElem imageElem = new ImageElem();
		imageElem.setUuid("uuid");
		imageElem.setImageFormat(0);

		ImageElem.ImageInfo imageInfo = new ImageElem.ImageInfo();
		imageInfo.setType(0);
		imageInfo.setSize(0);
		imageInfo.setWidth(0);
		imageInfo.setHeight(0);
		imageInfo.setUrl("url");

		imageElem.setImageInfos(Lists.newArrayList(imageInfo));

		String json = TencentImJson.getInstance().writeValueAsString(imageElem.body());
		System.out.println(json);
		Assertions.assertEquals(
				"{\"MsgType\":\"TIMImageElem\",\"MsgContent\":{\"UUID\":\"uuid\",\"ImageFormat\":0,\"ImageInfoArray\":[{\"Type\":0,\"Size\":0,\"Width\":0,\"Height\":0,\"URL\":\"url\"}]}}",
				json);
	}

}