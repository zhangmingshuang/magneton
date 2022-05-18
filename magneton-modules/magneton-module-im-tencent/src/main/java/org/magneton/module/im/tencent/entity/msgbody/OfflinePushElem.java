package org.magneton.module.im.tencent.entity.msgbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 离线推送消息
 *
 * https://cloud.tencent.com/document/product/269/2720#.E7.A6.BB.E7.BA.BF.E6.8E.A8.E9.80.81-offlinepushinfo-.E8.AF.B4.E6.98.8E
 *
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class OfflinePushElem {

	/**
	 * PushFlag Integer 选填 0表示推送，1表示不离线推送。
	 */
	@JsonProperty("PushFlag")
	private int pushFlag = 0;

	/**
	 * Title String 选填 离线推送标题。该字段为 iOS 和 Android 共用。
	 */
	@JsonProperty("Title")
	private String title;

	/**
	 * Desc String 选填 离线推送内容。该字段会覆盖上面各种消息元素 TIMMsgElement 的离线推送展示文本。 若发送的消息只有一个
	 * TIMCustomElem 自定义消息元素，该 Desc 字段会覆盖 TIMCustomElem 中的 Desc 字段。如果两个 Desc
	 * 字段都不填，将收不到该自定义消息的离线推送。
	 */
	@JsonProperty("Desc")
	private String desc;

	/**
	 * Ext String 选填 离线推送透传内容。
	 *
	 * 由于国内各 Android 手机厂商的推送平台要求各不一样，请保证此字段为 JSON 格式，否则可能会导致收不到某些厂商的离线推送。
	 */
	@Nullable
	@JsonProperty("Ext")
	private String ext;

	@Nullable
	@JsonProperty("AndroidInfo")
	private AndroidInfo androidInfo;

	@Nullable
	@JsonProperty("ApnsInfo")
	private ApnsInfo apnsInfo;

	@Setter
	@Getter
	@ToString
	public static class AndroidInfo {

		/**
		 * Sound String 选填 Android 离线推送声音文件路径。
		 */
		@Nullable
		@JsonProperty("Sound")
		private String sound;

		/**
		 * HuaWeiChannelID String 选填 华为手机 EMUI 10.0 及以上的通知渠道字段。该字段不为空时，会覆盖控制台配置的 ChannelID
		 * 值；该字段为空时，不会覆盖控制台配置的 ChannelID 值。
		 */
		@Nullable
		@JsonProperty("HuaWeiChannelID")
		private String huaWeiChannelID;

		/**
		 * XiaoMiChannelID String 选填 小米手机 MIUI 10 及以上的通知类别（Channel）适配字段。该字段不为空时，会覆盖控制台配置的
		 * ChannelID 值；该字段为空时，不会覆盖控制台配置的 ChannelID 值。
		 */
		@Nullable
		@JsonProperty("XiaoMiChannelID")
		private String xiaoMiChannelID;

		/**
		 * OPPOChannelID String 选填 OPPO 手机 Android 8.0 及以上的 NotificationChannel
		 * 通知适配字段。该字段不为空时，会覆盖控制台配置的 ChannelID 值；该字段为空时，不会覆盖控制台配置的 ChannelID 值。
		 */
		@Nullable
		@JsonProperty("OPPOChannelID")
		private String oppoChannelID;

		/**
		 * GoogleChannelID String 选填 Google 手机 Android 8.0 及以上的通知渠道字段。Google
		 * 推送新接口（上传证书文件）支持 channel id，旧接口（填写服务器密钥）不支持。
		 */
		@Nullable
		@JsonProperty("GoogleChannelID")
		private String googleChannelID;

		/**
		 * VIVOClassification Integer 选填 VIVO 手机推送消息分类，“0”代表运营消息，“1”代表系统消息，不填默认为1。
		 */
		@Nullable
		@JsonProperty("VIVOClassification")
		private String vIVOClassification;

		/**
		 * HuaWeiImportance String 选填 华为推送通知消息分类，取值为 LOW、NORMAL，不填默认为 NORMAL。
		 */
		@Nullable
		@JsonProperty("HuaWeiImportance")
		private String huaWeiImportance;

		/**
		 * ExtAsHuaweiIntentParam Integer 选填 在控制台配置华为推送为“打开应用内指定页面”的前提下，传“1”表示将透传内容 Ext 作为
		 * Intent 的参数，“0”表示将透传内容 Ext 作为 Action 参数。不填默认为0。两种传参区别可参见 华为推送文档。
		 */
		@Nullable
		@JsonProperty("ExtAsHuaweiIntentParam")
		private String extAsHuaweiIntentParam;

	}

	// 由于 APNs 推送限制数据包大小不能超过4K，因此除去其他控制字段，建议 Desc 和 Ext 字段之和不要超过3K。
	@Setter
	@Getter
	@ToString
	public static class ApnsInfo {

		/**
		 * BadgeMode Integer 选填 这个字段缺省或者为0表示需要计数，为1表示本条消息不需要计数，即右上角图标数字不增加。
		 */
		@Nullable
		@JsonProperty("BadgeMode")
		private Integer badgeMode;

		/**
		 * Title String 选填 该字段用于标识 APNs 推送的标题，若填写则会覆盖最上层 Title。
		 */
		@Nullable
		@JsonProperty("Title")
		private String title;

		/**
		 * SubTitle String 选填 该字段用于标识 APNs 推送的子标题。
		 */
		@Nullable
		@JsonProperty("SubTitle")
		private String subTitle;

		/**
		 * Image String 选填 该字段用于标识 APNs 携带的图片地址，当客户端拿到该字段时，可以通过下载图片资源的方式将图片展示在弹窗上。
		 */
		@Nullable
		@JsonProperty("Image")
		private String image;

		/**
		 * MutableContent Integer 选填 为1表示开启 iOS 10 的推送扩展，默认为0。
		 */
		@Nullable
		@JsonProperty("MutableContent")
		private Integer mutableContent;

	}

}
