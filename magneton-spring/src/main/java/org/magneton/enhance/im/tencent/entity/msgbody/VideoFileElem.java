package org.magneton.enhance.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 视频消息元素.
 *
 * 通过服务端集成的 Rest API 接口发送视频消息时，必须填入
 * VideoUrl、VideoUUID、ThumbUrl、ThumbUUID、ThumbWidth、ThumbHeight、VideoDownloadFlag 和
 * ThumbDownloadFlag字段。需保证通过 VideoUrl 能下载到对应视频，通过 ThumbUrl 能下载到对应的视频缩略图。VideoUUID 和
 * ThumbUUID 字段需填写全局唯一的 String 值，一般填入对应视频和视频缩略图的 MD5 值。消息接收者可以通过调用
 * V2TIMVideoElem.getVideoUUID() 和 V2TIMVideoElem.getSnapshotUUID() 分别拿到设置的 UUID 字段，业务 App
 * 可以用这个字段做视频的区分。VideoDownloadFlag 和 ThumbDownloadFlag 字段必须填2。
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
public class VideoFileElem implements MsgBodyElem {

	/** VideoUrl String 视频下载地址。可通过该 URL 地址直接下载相应视频。 */
	@JsonProperty("VideoUrl")
	private String videoUrl;

	/**
	 * VideoUUID String 视频的唯一标识，客户端用于索引视频的键值。
	 */
	@JsonProperty("VideoUUID")
	private String videoUuid;

	/**
	 * VideoSize Number 视频数据大小，单位：字节。
	 */
	@JsonProperty("VideoSize")
	private long videoSize;

	/**
	 * VideoSecond Number 视频时长，单位：秒。
	 */
	@JsonProperty("VideoSecond")
	private long videoSecond;

	/**
	 * VideoFormat String 视频格式，例如 mp4。
	 */
	@JsonProperty("VideoFormat")
	private String videoFormat;

	/**
	 * VideoDownloadFlag Number 视频下载方式标记。目前 VideoDownloadFlag 取值只能为2，表示可通过VideoUrl字段值的 URL
	 * 地址直接下载视频。
	 */
	@JsonProperty("VideoDownloadFlag")
	private int videoDownloadFlag;

	/**
	 * ThumbUrl String 视频缩略图下载地址。可通过该 URL 地址直接下载相应视频缩略图。
	 */
	@JsonProperty("ThumbUrl")
	private String thumbUrl;

	/**
	 * ThumbUUID String 视频缩略图的唯一标识，客户端用于索引视频缩略图的键值。
	 */
	@JsonProperty("ThumbUUID")
	private String thumbUuid;

	/**
	 * ThumbSize Number 缩略图大小，单位：字节。
	 */
	@JsonProperty("ThumbSize")
	private long thumbSize;

	/**
	 * ThumbWidth Number 缩略图宽度，单位为像素。
	 */
	@JsonProperty("ThumbWidth")
	private int thumbWidth;

	/**
	 * ThumbHeight Number 缩略图高度，单位为像素。
	 */
	@JsonProperty("ThumbHeight")
	private int thumbHeight;

	/**
	 * ThumbFormat String 缩略图格式，例如 JPG、BMP 等。
	 */
	@JsonProperty("ThumbFormat")
	private String thumbFormat;

	/**
	 * ThumbDownloadFlag Number 视频缩略图下载方式标记。目前 ThumbDownloadFlag 取值只能为2，表示可通过ThumbUrl字段值的
	 * URL 地址直接下载视频缩略图。
	 */
	@JsonProperty("ThumbDownloadFlag")
	private String thumbDownloadFlag;

	@Override
	public String getMsgType() {
		return "TIMVideoFileElem";
	}

}
