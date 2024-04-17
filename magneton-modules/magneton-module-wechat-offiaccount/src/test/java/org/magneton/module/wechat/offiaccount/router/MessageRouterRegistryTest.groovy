package org.magneton.module.wechat.offiaccount.router

import org.magneton.module.wechat.offiaccount.handler.MessageHandler
import spock.lang.Specification

/**
 * Test Case For {@link MessageRouterRegistry}.
 * @author zhangmsh.
 * @since 2024
 */
class MessageRouterRegistryTest extends Specification {

    void "泛型"() {
        given:
        def handler = Mock(MessageHandler)

        when:
        MessageRouterRegistry.registerMsgRoute(handler);
        then:
        MessageRouterRegistry.getMsgHandler() == handler
    }


}