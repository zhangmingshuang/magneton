package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 地理位置消息元素
 *
 * 当接收方为 iOS 或 Android，且应用处在后台时，中文版离线推送文本为“[位置]”，英文版离线推送文本为“[Location]”。
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class LocationElem implements MsgBodyElem {

	/** Desc String 地理位置描述信息。 **/
	@JsonProperty("Desc")
	private String desc;

	/** Latitude Number 纬度。 **/
	@JsonProperty("Latitude")
	private double latitude;

	/** Longitude Number 经度。 **/
	@JsonProperty("Longitude")
	private double longitude;

	@Override
	public String getMsgType() {
		return "TIMLocationElem";
	}

}
