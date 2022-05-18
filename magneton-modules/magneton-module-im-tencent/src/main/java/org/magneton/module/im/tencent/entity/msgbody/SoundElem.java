package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 语音消息元素.
 *
 * 通过服务端集成的 Rest API 接口发送语音消息时，必须填入语音的 Url、UUID、Download_Flag 字段。需保证通过 Url 能下载到对应语音。UUID
 * 字段需填写全局唯一的 String 值，一般填入语音文件的 MD5 值。消息接收者可以通过 V2TIMSoundElem.getUUID() 拿到设置的 UUID 字段，业务
 * App 可以用这个字段做语音的区分。Download_Flag 字段必须填2。
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SoundElem implements MsgBodyElem {

	/**
	 * Url String 语音下载地址，可通过该 URL 地址直接下载相应语音。
	 */
	@JsonProperty("Url")
	private String url;

	/**
	 * UUID String 语音的唯一标识，客户端用于索引语音的键值。
	 */
	@JsonProperty("UUID")
	private String uuid;

	/**
	 * Size Number 语音数据大小，单位：字节。
	 */
	@JsonProperty("Size")
	private int size;

	/**
	 * Second Number 语音时长，单位：秒。
	 */
	@JsonProperty("Second")
	private int second;

	/**
	 * Download_Flag Number 语音下载方式标记。目前 Download_Flag 取值只能为2，表示可通过Url字段值的 URL 地址直接下载语音。
	 */
	@JsonProperty("Download_Flag")
	private int downloadFlag;

	@Override
	public String getMsgType() {
		return "TIMSoundElem";
	}

}
