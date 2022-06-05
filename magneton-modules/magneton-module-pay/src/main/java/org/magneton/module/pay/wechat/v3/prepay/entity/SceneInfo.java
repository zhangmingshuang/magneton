package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
// 场景信息
public class SceneInfo {

	/**
	 * 用户终端IP string[1,45] 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。 示例值：14.23.150.211
	 **/
	@JsonProperty("payer_client_ip")
	private String payerClientIp;

	/**
	 * 商户端设备号 string[1,32] 商户端设备号（门店号或收银设备ID）。 示例值：013467007045764
	 **/
	@Nullable
	@JsonProperty("device_id")
	private String deviceId;

	/**
	 * 商户门店信息
	 */
	@Nullable
	@JsonProperty("store_info")
	private StoreInfo storeInfo;

}