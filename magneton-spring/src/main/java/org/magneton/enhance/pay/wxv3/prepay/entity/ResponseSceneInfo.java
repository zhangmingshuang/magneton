package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/4/6
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class ResponseSceneInfo {

	/**
	 * 商户端设备号 device_id string[1,32] 否 终端设备号（门店号或收银设备ID）。 示例值：013467007045764
	 **/
	@JsonProperty("device_id")
	@Nullable
	private String deviceId;

}
