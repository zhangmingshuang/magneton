package org.magneton.module.im.tencent.api;

import cn.hutool.http.HttpUtil;
import com.alibaba.testable.core.annotation.MockInvoke;
import com.alibaba.testable.core.model.MockScope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.im.tencent.TencentIm;
import org.magneton.module.im.tencent.TencentImConfig;
import org.magneton.module.im.tencent.TencentImJson;
import org.magneton.module.im.tencent.api.msg.MsgModel;
import org.magneton.module.im.tencent.entity.BatchSendMsg;
import org.magneton.module.im.tencent.entity.MsgSendRes;
import org.magneton.module.im.tencent.entity.msgbody.MsgBodyElem;
import org.magneton.module.im.tencent.entity.msgbody.OfflinePushElem;
import org.magneton.module.im.tencent.entity.msgbody.TextElem;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
class MsgImplTest {

	@Test
	void batchSend() throws JsonProcessingException {
		TencentImConfig config = new TencentImConfig();
		config.setAppId(1);
		config.setAppSecret("test");
		config.setAdmin("administrator");
		config.setUserSignExpireSeconds(10);

		TencentIm tencentIm = new TencentIm(config);
		BatchSendMsg msg = new BatchSendMsg();
		msg.setSyncOtherMachine(MsgModel.IGNORE_FROM_ACCOUNT.getCode());
		msg.setFromAccount("administrator");
		msg.setToAccount(Sets.newHashSet("nmhqabsikm8b"));
		msg.setMsgRandom(MsgBodyElem.random());
		msg.setMsgBody(Lists.newArrayList(new TextElem().setText("hello").body()));

		OfflinePushElem offlinePushElem = new OfflinePushElem();
		offlinePushElem.setTitle("test");
		offlinePushElem.setDesc("desc");
		msg.setOfflinePushInfo(offlinePushElem);

		MsgSendRes msgSendRes = tencentIm.msg().batchSendMsg(msg);
		System.out.println(msgSendRes);
		Assertions.assertTrue(msgSendRes.isOk());
	}

	public static class Mock {

		@SneakyThrows
		@MockInvoke(targetClass = HttpUtil.class, scope = MockScope.ASSOCIATED)
		public static String post(String urlString, String body) {
			MsgSendRes msgSendRes = new MsgSendRes();
			msgSendRes.setActionStatus("OK");
			msgSendRes.setErrorCode(0);
			msgSendRes.setErrorInfo("");
			msgSendRes.setMsgKey("test-key");
			return TencentImJson.getInstance().writeValueAsString(msgSendRes);
		}

	}

}