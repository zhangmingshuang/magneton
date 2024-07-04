package org.magneton.enhance.pay.wxv3.core;

/**
 * 与分账方的关系类型
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
public enum WxRelationType {

	/**
	 * 门店
	 */
	STORE,
	/**
	 * 员工
	 */
	STAFF,
	/**
	 * 店主
	 */
	STORE_OWNER,
	/**
	 * 合作伙伴
	 */
	PARTNER,
	/**
	 * 总部
	 */
	HEADQUARTER,
	/**
	 * 品牌方
	 */
	BRAND,
	/**
	 * 分销商
	 */
	DISTRIBUTOR,
	/**
	 * 用户
	 */
	USER,
	/**
	 * 供应商
	 */
	SUPPLIER,
	/**
	 * 自定义
	 */
	CUSTOM

}
