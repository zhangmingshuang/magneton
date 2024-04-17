package org.magneton.module.wechat.offiaccount.handler

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import spock.lang.Specification

/**
 * Test Case For {@link EventMessageProcessor}.
 * @author zhangmsh.
 * @since 2020/3/4.
 */
class EventMessageProcessorTest extends Specification {

    void "test onSubscribeEvent"() {
        given:
        def eventMessageProcessor = new EventMessageProcessor()
        def inMessage = WxMpXmlMessage.fromXml("<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "  <CreateTime>123456789</CreateTime>\n" +
                "  <MsgType><![CDATA[event]]></MsgType>\n" +
                "  <Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>");
        and:
        def handler = Mock(MessageHandler)
        when:
        def result = eventMessageProcessor.doProcess(handler, inMessage)
        then:
        result == null
    }
}