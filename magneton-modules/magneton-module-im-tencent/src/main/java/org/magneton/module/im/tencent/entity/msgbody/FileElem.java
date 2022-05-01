package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件消息元素.
 *
 * 通过服务端集成的 Rest API 接口发送文件消息时，需要填入文件的 Url、UUID、Download_Flag 字段。需保证通过该 Url 能下载到对应文件。UUID
 * 字段需填写全局唯一的 String 值，一般填入文件的 MD5 值。消息接收者可以通过调用 V2TIMFileElem.getUUID() 拿到设置的 UUID 字段，业务
 * App 可以用这个字段做文件的区分。Download_Flag字段必须填2。
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class FileElem implements MsgBodyElem {

	/**
	 * Url String 文件下载地址，可通过该 URL 地址直接下载相应文件。
	 */
	@JsonProperty("Url")
	private String url;

	/**
	 * UUID String 文件的唯一标识，客户端用于索引文件的键值。
	 */
	@JsonProperty("UUID")
	private String uuid;

	/**
	 * FileSize Number 文件数据大小，单位：字节。
	 */
	@JsonProperty("FileSize")
	private Long fileSize;

	/**
	 * FileName String 文件名称。
	 */
	@JsonProperty("FileName")
	private String fileName;

	/**
	 * Download_Flag Number 文件下载方式标记。目前 Download_Flag 取值只能为2，表示可通过Url字段值的 URL 地址直接下载文件。
	 */
	@JsonProperty("Download_Flag")
	private int downloadFlag;

	@Override
	public String getMsgType() {
		return "TIMFileElem";
	}

}
