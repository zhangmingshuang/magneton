package org.magneton.module.im.tencent.api;

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
	 * @apiNote 支持一次对最多500个用户进行单发消息。
	 * {@code https://cloud.tencent.com/document/product/269/2282}
	 */
	MsgSendRes batchSendMsg(BatchSendMsg batchSendMsg);

}