# 支付实践

## 支付配置

```yaml
magneton:
  module:
    pay:
      weixin:
        merchant-id: xxx
        api-v3-key: xxxx
        merchant-serial-number: xxxx
        merchant-private-key-file: /wechat/cert/apiclient_key.pem
        notify-url: http://xxx.com/api/pay/wechatcb
        app-id:
          app: xxx #配置APPID
          jsapi: xxx #配置JSAPIID
```

## APP支付代码

```java
        WxPayAppPrepayReq appPrepayReq=new WxPayAppPrepayReq();
		appPrepayReq.setOutTradeNo("支付订单号");
		appPrepayReq.setAmount(new Amount().setTotal(amount));
		appPrepayReq.setDescription("支付描述信息");

		//设置是允分账
		SettleInfo settleInfo=new SettleInfo();
		settleInfo.setProfitSharing(true);
		appPrepayReq.setSettleInfo(settleInfo);
		//发起预支付
		Consequences<WxPayAppPrepay> appPreparyRes=this.wxPay.appPrepay().prepay(appPrepayReq);
```

### JSAPI支付代码

```java
        WxPayJSAPIPrepayReq jsapiPrepayReq=new WxPayJSAPIPrepayReq();
		jsapiPrepayReq.setOutTradeNo(outTradeNo);
		jsapiPrepayReq.setAmount(new Amount().setTotal(minTaskAmount));
		jsapiPrepayReq.setDescription(this.lookProperties.getPayDescription());
		//设置支付者信息
		Payer payer=new Payer();
		payer.setOpenid(wxOpenId);
		jsapiPrepayReq.setPayer(payer);
		//设置分账
		SettleInfo settleInfo=new SettleInfo();
		settleInfo.setProfitSharing(true);
		jsapiPrepayReq.setSettleInfo(settleInfo);
		//发起预支付
		Consequences<WxPayJSAPIPrepay> jsApiPreparyRes=this.wxPay.jsapiPrepay().prepay(jsapiPrepayReq);
```

## 支付回调处理

控制层

```java
        public String wechat(HttpServletRequest request){
		return this.payService.wechatPayCallback(request);
		}
```

服务层

```java
        Map<String, String> headers=HttpRequest.getHeaders(request);
		String body=HttpRequest.getBody(request);
		//解析支付通知数据
		WxPayNotification payNotification=this.wxPay.parsePaySuccessData(headers,body);
```