package org.magneton.module.wechat.mp.core.handler

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.magneton.module.wechat.mp.MsgConst
import org.magneton.module.wechat.mp.core.mode.EventPushMode
import spock.lang.Specification

/**
 * Test Case For {@link EventPushDispatchProcessor}.
 * @author zhangmsh.
 * @since 2020/3/4.
 */
class EventPushDispatchProcessorTest extends Specification {

    void "test onSubscribeEvent"() {
        given:
        def eventMessageProcessor = new EventPushDispatchProcessor()
        def inMessage = WxMpXmlMessage.fromXml(MsgConst.EVENT_SUBSCRIBE);
        and:
        def handler = Mock(MpHandler)
        when:
        def result = eventMessageProcessor.doProcess(EventPushMode.SUBSCRIBE, handler, inMessage)
        then:
        result == null
    }
}