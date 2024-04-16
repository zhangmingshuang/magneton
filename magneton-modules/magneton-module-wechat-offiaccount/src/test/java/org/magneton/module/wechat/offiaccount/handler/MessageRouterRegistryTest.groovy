package org.magneton.module.wechat.offiaccount.handler

import org.magneton.module.wechat.offiaccount.pojo.InputTextMsg
import org.magneton.module.wechat.offiaccount.pojo.OutputTextMsg
import org.magneton.module.wechat.offiaccount.router.MessageRouterRegistry
import spock.lang.Specification

/**
 * Test Case For {@link MessageRouterRegistry}.
 * @author zhangmsh.
 * @since 2024
 */
class MessageRouterRegistryTest extends Specification {

    void "泛型"() {
        given:
        def handler = new TestMessageHandler()

        when:
        MessageRouterRegistry.registerMsgRoute(handler);
        then:
        MessageRouterRegistry.getMsgHandler() == handler
    }

    public static class TestMessageHandler implements MessageHandler {

        @Override
        OutputTextMsg onTextMsg(InputTextMsg textMsg) {
            return null
        }
    }

}