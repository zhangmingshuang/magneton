package org.magneton.module.im.tencent.api.msg;

import org.magneton.module.im.tencent.entity.BatchSendMsg;
import org.magneton.module.im.tencent.entity.MsgSendRes;

/**
 * .
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
public interface Msg {

	/**
	 * 单发单聊消息
	 * @param batchSendMsg 批量发单聊消息
	 * @apiNote 支持一次对最多500个用户进行单发消息。
	 * {@code https://cloud.tencent.com/document/product/269/2282}
	 * @return 发送结果
	 */
	MsgSendRes batchSendMsg(BatchSendMsg batchSendMsg);

}