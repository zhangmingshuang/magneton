package org.magneton.module.wechat.mp.core.router

import org.magneton.module.wechat.mp.core.message.handler.MpHandler
import org.magneton.module.wechat.mp.core.message.handler.MpHandlerRegistrar
import org.magneton.module.wechat.mp.core.message.mode.StandardMsgMode
import spock.lang.Specification

/**
 * Test Case For {@link MpHandlerRegistrar}.
 * @author zhangmsh.
 * @since 2024
 */
class MpHandlerRegistrarTest extends Specification {

    void "注册Handler"() {
        given:
        def handler = Mock(MpHandler)
        handler.supportMode() >> [StandardMsgMode.TEXT]

        when:
        MpHandlerRegistrar.register(handler);
        then:
        MpHandlerRegistrar.getHandler(StandardMsgMode.TEXT) == handler
        MpHandlerRegistrar.getHandler(StandardMsgMode.IMAGE) == null

        cleanup:
        MpHandlerRegistrar.unregister(handler)
    }

    void "批量注册Handler"() {
        given:
        def handler = Mock(MpHandler)
        handler.supportMode() >> [StandardMsgMode.TEXT, StandardMsgMode.IMAGE]

        when:
        MpHandlerRegistrar.register(handler);
        then:
        MpHandlerRegistrar.getHandler(StandardMsgMode.TEXT) == handler
        MpHandlerRegistrar.getHandler(StandardMsgMode.IMAGE) == handler
        MpHandlerRegistrar.getHandler(StandardMsgMode.VOICE) == null

        cleanup:
        MpHandlerRegistrar.unregister(handler)
    }


}