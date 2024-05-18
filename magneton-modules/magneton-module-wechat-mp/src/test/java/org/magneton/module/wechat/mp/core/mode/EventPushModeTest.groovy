package org.magneton.module.wechat.mp.core.mode

import cn.hutool.json.JSONUtil
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.magneton.module.wechat.mp.MsgConst
import org.magneton.module.wechat.mp.core.message.mode.EventPushMode
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Test Case For {@link org.magneton.module.wechat.mp.core.message.mode.EventPushMode}.
 * @author zhangmsh.
 * @since 2024
 */
class EventPushModeTest extends Specification {

    @Unroll("#type")
    void "test"() {
        given:
        def inMessage = WxMpXmlMessage.fromXml(msg);
        when:
        def eventMode = EventPushMode.of(inMessage.getEvent(), inMessage.getEventKey())
        then:
        eventMode == type

        where:
        msg                             | type
        MsgConst.EVENT_SUBSCRIBE        | EventPushMode.SUBSCRIBE
        MsgConst.EVENT_UNSUBSCRIBE      | EventPushMode.UNSUBSCRIBE
        MsgConst.EVENT_SCAN_UNSUBSCRIBE | EventPushMode.SCAN_UNSUBSCRIBE
        MsgConst.EVENT_SCAN_SUBSCRIBE   | EventPushMode.SCAN_SUBSCRIBE
        MsgConst.EVENT_LOCATION         | EventPushMode.LOCATION
        MsgConst.EVENT_CLICK            | EventPushMode.CLICK
        MsgConst.EVENT_VIEW             | EventPushMode.VIEW
    }

    void "testUnsubscribe"() {
        given:
        def inMessage = JSONUtil.toBean("{\"allFieldsMap\":{\"CreateTime\":\"1713506579\",\"EventKey\":\"\",\"Event\":\"unsubscribe\",\"ToUserName\":\"gh_00b1c6359ec2\",\"FromUserName\":\"oeCaH1dbxZiip4mWjH_0d1cWWiLI\",\"MsgType\":\"event\"},\"toUser\":\"gh_00b1c6359ec2\",\"fromUser\":\"oeCaH1dbxZiip4mWjH_0d1cWWiLI\",\"createTime\":1713506579,\"msgType\":\"event\",\"event\":\"unsubscribe\",\"eventKey\":\"\",\"scanCodeInfo\":{},\"sendPicsInfo\":{\"picList\":[]},\"sendLocationInfo\":{},\"articleUrlResult\":{},\"hardWare\":{}}",
                WxMpXmlMessage.class);
        when:
        def eventMode = EventPushMode.of(inMessage.getEvent(), inMessage.getEventKey())
        then:
        println eventMode
    }
}