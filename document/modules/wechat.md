# 微信

## APP

待补充

## 小程序

- 引用

```xml

<dependencies>
	<dependency>
		<groupId>org.magneton</groupId>
		<artifactId>magneton-spring-boot-starter</artifactId>
		<version>xxx</version>
	</dependency>

	<dependency>
		<groupId>org.magneton</groupId>
		<artifactId>magneton-module-wechat</artifactId>
		<version>xxx</version>
	</dependency>
</dependencies>
```

详见: `WechatMiniProgramAutoConfiguration`

- 配置

```yaml
magneton:
  module:
    wechat:
      mini-program:
        appid: xxx
        secret: xxxx
```

- 交互
  前端通过授权获取到`code`及相关的用户信息`userInfo`

1. 获取unionId与openId

```
Consequences<MPCode2Session> reply=this.wechatMiniProgram.code2Session(code);
MPCode2Session session=reply.getData();
String unionId=session.getUnionid();
String openId=session.getOpenid();
```

2. 转换用户信息

```
MPUserInfo mpUserInfo = JSON.parseObject(userInfo, MPUserInfo.class);
Consequences<MPSensitiveUserInfo> mpSensitiveUserInfoConsequences = this.wechatMiniProgram
						.decodeUserInfo(session.getSessionKey(), mpUserInfo);
MPSensitiveUserInfo sensitiveUserInfo = mpSensitiveUserInfoConsequences.getData();
```