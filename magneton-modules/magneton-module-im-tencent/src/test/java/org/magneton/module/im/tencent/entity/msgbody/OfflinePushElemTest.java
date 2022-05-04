package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.magneton.module.im.tencent.TencentImJson;
import org.magneton.module.im.tencent.entity.BatchSendMsg;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
class OfflinePushElemTest {

	@Test
	void test() throws JsonProcessingException {
		BatchSendMsg msg = new BatchSendMsg();

		OfflinePushElem offlinePushElem = new OfflinePushElem();
		offlinePushElem.setTitle("test");
		offlinePushElem.setDesc("test desc");
		msg.setOfflinePushInfo(offlinePushElem);

		String json = TencentImJson.getInstance().writeValueAsString(msg);
		System.out.println(json);
	}

}