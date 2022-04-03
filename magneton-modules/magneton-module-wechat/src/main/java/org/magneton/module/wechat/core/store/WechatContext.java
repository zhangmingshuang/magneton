// package org.magneton.module.wechat.core.store;
//
// import org.magneton.core.base.Preconditions;
// import org.magneton.module.wechat.core.oauth2.AccessToken;
//
/// **
// * 微信上下文，用来共享相关的微信数据。
// *
// * @author zhangmsh 2022/4/1
// * @since 1.0.0
// */
// public class WechatContext {
//
// private static final WechatContext INSTANCE = new WechatContext();
//
// private TokenCache tokenCache = new MemoryTokenCache();
//
// private WechatContext() {
//
// }
//
// public AccessToken getCachedAccessToken(String code) {
// this.validate();
// Preconditions.checkNotNull(code);
// return this.tokenCache.get(code);
// }
//
// public void setCachedAccessToken(String code, AccessToken accessToken) {
// this.validate();
// Preconditions.checkNotNull(code);
// this.tokenCache.set(code, accessToken);
// }
//
// private void validate() {
// Preconditions.checkNotNull(this.tokenCache, "WechatTokenCache must not be null");
// }
//
// public void setWechatTokenCache(TokenCache tokenCache) {
// this.tokenCache = Preconditions.checkNotNull(tokenCache);
// }
//
// }
