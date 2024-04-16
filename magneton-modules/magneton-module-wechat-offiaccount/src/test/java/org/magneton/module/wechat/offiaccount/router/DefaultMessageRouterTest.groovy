package org.magneton.module.wechat.offiaccount.router

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage
import org.magneton.module.wechat.offiaccount.core.OffiaccountContext
import org.magneton.module.wechat.offiaccount.handler.MessageHandler
import org.magneton.module.wechat.offiaccount.pojo.InputTextMsg
import org.magneton.module.wechat.offiaccount.pojo.OutputTextMsg
import spock.lang.Specification

/**
 * Test Case For {@link DefaultMessageRouter}.
 * @author zhangmsh.
 * @since 2024
 */
class DefaultMessageRouterTest extends Specification {

    void "普通消息"() {
        given:
        def context = Mock(OffiaccountContext)
        def messageRouter = new DefaultMessageRouter(context)
        def msg = "<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "  <CreateTime>1348831860</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[this is a test]]></Content>\n" +
                "  <MsgId>1234567890123456</MsgId>\n" +
                "  <MsgDataId>xxxx</MsgDataId>\n" +
                "  <Idx>xxxx</Idx>\n" +
                "</xml>";
        when:
        def result = messageRouter.route(WxMpXmlMessage.fromXml(msg))
        then:
        result == null

        when: "注册处理器"
        MessageRouterRegistry.registerMsgRoute(new TestMessageHandler())
        and:
        result = messageRouter.route(WxMpXmlMessage.fromXml(msg))
        then:
        result != null
        ((WxMpXmlOutTextMessage) result).getContent() == "this is a test"
    }

    public static class TestMessageHandler implements MessageHandler {

        @Override
        OutputTextMsg onTextMsg(InputTextMsg inputTextMsg) {
            OutputTextMsg outputTextMsg = new OutputTextMsg();
            outputTextMsg.setContent(inputTextMsg.getContent());
            return outputTextMsg;
        }
    }
}