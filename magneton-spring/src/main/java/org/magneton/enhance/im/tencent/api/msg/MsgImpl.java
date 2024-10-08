package org.magneton.enhance.im.tencent.api.msg;

import com.dtflys.forest.Forest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.magneton.enhance.im.tencent.TencentIm;
import org.magneton.enhance.im.tencent.TencentImConfig;
import org.magneton.enhance.im.tencent.TencentImJson;
import org.magneton.enhance.im.tencent.entity.BatchSendMsg;
import org.magneton.enhance.im.tencent.entity.MsgSendRes;
import org.magneton.enhance.im.tencent.exception.MsgSendException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Slf4j
public class MsgImpl implements Msg {

	private final TencentIm tencentIm;

	public MsgImpl(TencentIm tencentIm) {
		this.tencentIm = tencentIm;
	}

	@Override
	public MsgSendRes batchSendMsg(BatchSendMsg batchSendMsg) {
		TencentImConfig config = this.tencentIm.getConfig();

		int random = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

		String url = String.format(
				"https://console.tim.qq.com/v4/openim/batchsendmsg?sdkappid=%s"
						+ "&identifier=%s&usersig=%s&random=%s&contenttype=json",
				config.getAppId(), config.getAdmin(), this.tencentIm.getAdminUserSign(), random);
		// 若不希望将消息同步至 From_Account，则 SyncOtherMachine 填写2。
		// 若希望将消息同步至 From_Account，则 SyncOtherMachine 填写1。
		try {
			String body = TencentImJson.getInstance().writeValueAsString(batchSendMsg);
			if (log.isDebugEnabled()) {
				log.debug("batchSendMsg url: {}, data:{}", url, body);
			}
			String response = Forest.post(url).addBody(body).setContentType("application/json").executeAsString();
			return TencentImJson.getInstance().readValue(response, MsgSendRes.class);
		}
		catch (JsonProcessingException e) {
			log.error("batchSendMsg error. url " + url + ", data:" + batchSendMsg, e);
			throw new MsgSendException(e);
		}
	}

}
