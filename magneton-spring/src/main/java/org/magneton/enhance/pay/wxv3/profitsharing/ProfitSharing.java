package org.magneton.enhance.pay.wxv3.profitsharing;

import org.magneton.enhance.Result;
import org.magneton.enhance.pay.wxv3.core.BaseV3Api;
import org.magneton.enhance.pay.wxv3.profitsharing.entity.*;

/**
 * 分账API
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
public interface ProfitSharing extends BaseV3Api {

	/**
	 * 请求分账
	 * @apiNote 微信订单支付成功后，商户发起分账请求，将结算后的资金分到分账接收方
	 *
	 * 注意：
	 * <ul>
	 * <li>• 对同一笔订单最多能发起50次分账请求，每次请求最多分给50个接收方</li>
	 * <li>• 此接口采用异步处理模式，即在接收到商户请求后，优先受理请求再异步处理，最终的分账结果可以通过查询分账接口获取</li>
	 * <li>• 商户需确保向微信支付传输用户身份信息和账号标识信息做一致性校验已合法征得用户授权</li>
	 * </ul>
	 * 频率限制：300/s 幂等规则：接口支持幂等重入
	 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter8_1_1.shtml">官网地址</a>
	 * @param req 请求参数
	 * @return 分账结果
	 */
	Result<WxProfitSharingOrders> orders(WxProfitSharingOrdersReq req);

	/**
	 * 查询分账结果
	 * @apiNote 发起分账请求后，可调用此接口查询分账结果
	 *
	 * 注意： 发起解冻剩余资金请求后，可调用此接口查询解冻剩余资金的结果
	 *
	 * <a href=
	 * "https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter8_1_2.shtml">官网地址</a>
	 * @param query 请求参数
	 * @return 分账结果
	 */
	Result<WxProfitSharingOrderState> state(WxProfitSharingOrderStateQuery query);

	/**
	 * 添加分账接收方
	 * @apiNote 商户发起添加分账接收方请求，建立分账接收方列表。后续可通过发起分账请求，将分账方商户结算后的资金，分到该分账接收方
	 *
	 * 注意： • 商户需确保向微信支付传输用户身份信息和账号标识信息做一致性校验已合法征得用户授权
	 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter8_1_8.shtml">官网地址</a>
	 * @param req 请求参数AbstractSignSafeDog
	 * @return 添加结果
	 */
	Result<WxProfitSharingReceiverAdd> add(WxProfitSharingReceiverAddReq req);

}
