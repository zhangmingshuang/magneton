package org.magneton.module.pay.wechat.v3.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotations.Description;

/**
 * 微信安全标识，表示需要加密
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
@Description("微信安全标识，表示需要加密")
public @interface WxSecret {

}
