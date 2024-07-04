package org.magneton.enhance.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 图像消息元素.
 *
 * 通过服务端集成的 Rest API 接口发送图像消息时，必须填入图像的以下字段：URL、UUID、Width、Height。需保证通过 URL 能下载到对应图像。可据此
 * 获取图片基本信息 并 处理图片。Width 和 Height 分别为图片的宽度和高度，单位为像素。UUID 字段需填写全局唯一的 String 值，一般填入图片的 MD5
 * 值。消息接收者通过调用 V2TIMImageElem.getImageList() 拿到 V2TIMImage 对象，然后通过调用V2TIMImage.getUUID()
 * 拿到设置的 UUID 字段，业务 App 可以用这个字段做图片的区分。
 *
 * @author zhangmsh 2022/4/30
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class ImageElem implements MsgBodyElem {

	/**
	 * UUID String 图片的唯一标识，客户端用于索引图片的键值。
	 */
	@JsonProperty("UUID")
	private String uuid;

	/**
	 * ImageFormat Number 图片格式。JPG = 1，GIF = 2，PNG = 3，BMP = 4，其他 = 255。
	 */
	@JsonProperty("ImageFormat")
	private int imageFormat;

	/**
	 * ImageInfoArray Array 原图、缩略图或者大图下载信息。
	 */
	@JsonProperty("ImageInfoArray")
	private List<ImageInfo> imageInfos;

	@Override
	public String getMsgType() {
		return "TIMImageElem";
	}

	@Setter
	@Getter
	@ToString
	public static class ImageInfo {

		/**
		 * Type Number 图片类型： 1-原图，2-大图，3-缩略图。
		 */
		@JsonProperty("Type")
		private int type;

		/**
		 * Size Number 图片数据大小，单位：字节。
		 */
		@JsonProperty("Size")
		private int size;

		/**
		 * Width Number 图片宽度，单位为像素。
		 */
		@JsonProperty("Width")
		private int width;

		/**
		 * Height Number 图片高度，单位为像素。
		 */
		@JsonProperty("Height")
		private int height;

		/**
		 * URL String 图片下载地址。
		 */
		@JsonProperty("URL")
		private String url;

	}

}
