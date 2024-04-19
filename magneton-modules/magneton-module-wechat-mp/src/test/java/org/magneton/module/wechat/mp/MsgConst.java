package org.magneton.module.wechat.mp;

/**
 * 消息常量.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class MsgConst {

	/**
	 * 普通消息.
	 */
	public static final String STANDARD_MSG = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" + "  <CreateTime>1348831860</CreateTime>\n"
			+ "  <MsgType><![CDATA[text]]></MsgType>\n" + "  <Content><![CDATA[this is a test]]></Content>\n"
			+ "  <MsgId>1234567890123456</MsgId>\n" + "  <MsgDataId>xxxx</MsgDataId>\n" + "  <Idx>xxxx</Idx>\n"
			+ "</xml>";

	/**
	 * 事件消息. 关注事件
	 */
	public static String EVENT_SUBSCRIBE = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[subscribe]]></Event>\n" + "</xml>";

	/**
	 * 事件消息. 取消关注事件
	 */
	public static String EVENT_UNSUBSCRIBE = EVENT_SUBSCRIBE.replace("subscribe", "unsubscribe");

	/**
	 * 扫描带参数二维码事件. 用户未关注时，进行关注后的事件推送
	 */
	public static String EVENT_SCAN_UNSUBSCRIBE = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[subscribe]]></Event>\n"
			+ "  <EventKey><![CDATA[qrscene_123123]]></EventKey>\n" + "  <Ticket><![CDATA[TICKET]]></Ticket>\n"
			+ "</xml>";

	/**
	 * 扫描带参数二维码事件. 用户已关注时的事件推送
	 */
	public static final String EVENT_SCAN_SUBSCRIBE = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[SCAN]]></Event>\n"
			+ "  <EventKey><![CDATA[SCENE_VALUE]]></EventKey>\n" + "  <Ticket><![CDATA[TICKET]]></Ticket>\n"
			+ "</xml> ";

	/**
	 * 上报地理位置
	 */
	public static final String EVENT_LOCATION = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[LOCATION]]></Event>\n"
			+ "  <Latitude>23.137466</Latitude>\n" + "  <Longitude>113.352425</Longitude>\n"
			+ "  <Precision>119.385040</Precision>\n" + "</xml>";

	/**
	 * 自定义菜单事件
	 */
	public static final String EVENT_CLICK = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[CLICK]]></Event>\n"
			+ "  <EventKey><![CDATA[EVENTKEY]]></EventKey>\n" + "</xml>";

	/**
	 * 点击菜单跳转链接时的事件推送
	 */
	public static final String EVENT_VIEW = "<xml>\n" + "  <ToUserName><![CDATA[toUser]]></ToUserName>\n"
			+ "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" + "  <CreateTime>123456789</CreateTime>\n"
			+ "  <MsgType><![CDATA[event]]></MsgType>\n" + "  <Event><![CDATA[VIEW]]></Event>\n"
			+ "  <EventKey><![CDATA[www.qq.com]]></EventKey>\n" + "</xml>";

}