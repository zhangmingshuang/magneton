package org.magneton.module.wechat.mp.core.router

import com.google.common.base.VerifyException
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage
import org.magneton.module.wechat.mp.MsgConst
import org.magneton.module.wechat.mp.core.MpContext
import org.magneton.module.wechat.mp.core.message.handler.MpHandler
import org.magneton.module.wechat.mp.core.message.handler.MpHandlerRegistrar
import org.magneton.module.wechat.mp.core.message.mode.StandardMsgMode
import org.magneton.module.wechat.mp.core.message.pojo.MpInTextMsg
import org.magneton.module.wechat.mp.core.message.pojo.MpOutTextMsg
import spock.lang.Specification

/**
 * Test Case For {@link DefaultDispatchRouter}.
 * @author zhangmsh.
 * @since 2024
 */
class DefaultDispatchRouterTest extends Specification {

    void "普通消息"() {
        given:
        def context = Mock(MpContext)
        def messageRouter = new DefaultDispatchRouter(context)
        def msg = MsgConst.STANDARD_MSG
        and:
        def handler = Mock(MpHandler)
        handler.supportMode() >> [StandardMsgMode.TEXT]
        handler.onTextMsg(_ as MpInTextMsg) >> new MpOutTextMsg(content: "this is a test")

        when:
        messageRouter.dispatch(WxMpXmlMessage.fromXml(msg))
        then: "未注册处理器"
        thrown(VerifyException)

        when: "注册处理器"
        MpHandlerRegistrar.register(handler)
        and:
        def result = messageRouter.dispatch(WxMpXmlMessage.fromXml(msg))
        then:
        result != null
        ((WxMpXmlOutTextMessage) result).getContent() == "this is a test"
    }
}