package org.magneton.enhance.pay.wxv3.profitsharing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * 添加分账接收方
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WxProfitSharingReceiverAdd {

	/**
	 * 分账接收方类型 type string[1, 32] 是
	 *
	 * 枚举值： MERCHANT_ID：商户ID PERSONAL_OPENID：个人openid（由父商户APPID转换得到）
	 *
	 * 示例值：MERCHANT_ID
	 * @see org.magneton.enhance.pay.wxv3.core.WxReceiverType
	 */
	private String type;

	/**
	 * 分账接收方账号 account string[1, 64] 是
	 *
	 * 类型是MERCHANT_ID时，是商户号 类型是PERSONAL_OPENID时，是个人openid openid获取方法
	 *
	 * 示例值：86693852
	 */
	private String account;

	/**
	 * 分账个人接收方姓名 name string[1, 1024] 否
	 * 分账接收方类型是MERCHANT_ID时，是商户全称（必传），当商户是小微商户或个体户时，是开户人姓名
	 * 分账接收方类型是PERSONAL_OPENID时，是个人姓名（选传，传则校验）
	 *
	 * <ul>
	 * <li>1、此字段需要加密，加密方法详见：敏感信息加密说明</li>
	 * <li>2、使用微信支付平台证书中的公钥：获取平台证书</li>
	 * <li>3、使用RSAES-OAEP算法进行加密</li>
	 * <li>4、将请求中HTTP头部的Wechatpay-Serial设置为证书序列号 示例值：hu89ohu89ohu89o</li>
	 * </ul>
	 */
	@Nullable
	private String name;

	/**
	 * 与分账方的关系类型 relation_type string[1, 32] 是
	 *
	 * 商户与接收方的关系。
	 *
	 * 本字段值为枚举： STORE：门店 STAFF：员工 STORE_OWNER：店主 PARTNER：合作伙伴 HEADQUARTER：总部 BRAND：品牌方
	 * DISTRIBUTOR：分销商 USER：用户 SUPPLIER： 供应商 CUSTOM：自定义 示例值：STORE
	 * @see org.magneton.enhance.pay.wxv3.core.WxRelationType
	 */
	@JsonProperty("relation_type")
	private String relationType;

	/**
	 * 自定义的分账关系 custom_relation string[1, 10] 否 子商户与接收方具体的关系，本字段最多10个字。
	 *
	 * 当字段relation_type的值为CUSTOM时，本字段必填;
	 *
	 * 当字段relation_type的值不为CUSTOM时，本字段无需填写。
	 *
	 * 示例值：代理商
	 */
	@Nullable
	@JsonProperty("custom_relation")
	private String customRelation;

}
