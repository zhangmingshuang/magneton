package org.magneton.module.im.tencent.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.im.tencent.api.msg.MsgModel;
import org.magneton.module.im.tencent.entity.msgbody.MsgBodyElem;
import org.magneton.module.im.tencent.entity.msgbody.OfflinePushElem;

import javax.annotation.Nullable;
import javax.annotation.SizeLimit;
import java.util.List;
import java.util.Set;

/**
 * 批量发单聊消息
 *
 * @author zhangmsh 2022/4/20
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class BatchSendMsg {

	/**
	 * 1：把消息同步到 From_Account 在线终端和漫游上
	 *
	 * 2：消息不同步至 From_Account；若不填写默认情况下会将消息存 From_Account 漫游
	 */
	@JsonProperty("SyncOtherMachine")
	private int syncOtherMachine = MsgModel.TO_FROM_ACCOUNT.getCode();

	/**
	 * From_Account String 选填
	 *
	 * 管理员指定消息发送方帐号（若需设置 From_Account 信息，则该参数取值不能为空）
	 *
	 * From_Accout 为管理员指定的发送方，接收方看到发送者不是管理员，而是 From_Account。
	 */
	@Nullable
	@JsonProperty("From_Account")
	private String fromAccount;

	/**
	 * To_Account Array 必填 消息接收方用户 UserID
	 **/
	@SizeLimit(500)
	@JsonProperty("To_Account")
	private Set<String> toAccount;

	/**
	 * MsgSeq Integer 选填
	 * 消息序列号（32位无符号整数），后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数
	 **/
	@Nullable
	@JsonProperty("MsgSeq")
	private Integer msgSeq;

	/**
	 * MsgRandom Integer 必填 消息随机数（32位无符号整数），后台用于同一秒内的消息去重。请确保该字段填的是随机
	 */
	@JsonProperty("MsgRandom")
	private Integer msgRandom;

	/**
	 * MsgBody Array 必填 TIM 消息，请参考 消息格式描述
	 *
	 */
	@JsonProperty("MsgBody")
	private List<MsgBodyElem.Body> msgBody;

	/**
	 * MsgLifeTime Integer 选填 消息离线保存时长（单位：秒），最长为7天（604800秒）。若设置该字段为0，则消息只发在线用户，不保存离线及漫游
	 *
	 */
	@Nullable
	@JsonProperty("MsgLifeTime")
	private Integer msgLifeTime;

	/**
	 * CloudCustomData String 选填 消息自定义数据（云端保存，会发送到对端，程序卸载重装后还能拉取到）
	 *
	 */
	@Nullable
	@JsonProperty("CloudCustomData")
	private String cloudCustomData;

	/**
	 * SendMsgControl Array 选填 消息发送控制选项，是一个 String
	 * 数组，只对本次请求有效。"NoUnread"表示该条消息不计入未读数。"NoLastMsg"表示该条消息不更新会话列表。"WithMuteNotifications"表示该条消息的接收方对发送方设置的免打扰选项生效（默认不生效）。示例："SendMsgControl":
	 * ["NoUnread","NoLastMsg","WithMuteNotifications"]
	 **/
	@Nullable
	@JsonProperty("SendMsgControl")
	private List<String> sendMsgControl;

	/** OfflinePushInfo Object 选填 离线推送信息配置，具体可参考 消息格式描述 **/
	@Nullable
	@JsonProperty("OfflinePushInfo")
	private OfflinePushElem offlinePushInfo;

}
