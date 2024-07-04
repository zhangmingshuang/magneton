package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Setter
@Getter
@ToString
@Accessors(chain = true)
// 商户门店信息
public class StoreInfo {

	/**
	 * 门店编号 string[1,32] 商户侧门店编号 示例值：0001
	 **/
	private String id;

	/**
	 * 门店名称 string[1,256] 商户侧门店名称 示例值：腾讯大厦分店
	 **/
	@Nullable
	private String name;

	/**
	 * 地区编码 string[1,32] 地区编码，详细请见省市区编号对照表。 示例值：440305
	 **/
	@Nullable
	@JsonProperty("area_code")
	private String areaCode;

	/**
	 * 详细地址 string[1,512] 详细的商户门店地址 示例值：广东省深圳市南山区科技中一道10000号
	 **/
	@Nullable
	private String address;

}