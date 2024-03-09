package org.magneton.module.pay.wechat.v3;

import org.junit.jupiter.api.Test;
import org.magneton.module.pay.wechat.v3.core.WxPayConfig;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhangmsh 2022/4/22
 * @since 1.0.0
 */
class WxV3PayImplTest {

	@Test
	void test() {
		String filePath = "D:\\workenv\\workspace\\look\\document\\cert\\apiclient_key.pem";
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			filePath = "/home/looker/cert/apiclient_key.pem";
		}
		if (!Files.exists(Paths.get(filePath))) {
			System.out.println("Test Error: 文件不存在");
			return;
		}

		String appId = "wx8df7293808c36e97";
		WxPayConfig config = new WxPayConfig();
		config.setAppId(appId);
		config.setMerchantId("1623995578");
		config.setMerchantSerialNumber("2DBDE5AE7A7DA4F9AC371C21100A88E558DF1892");
		config.setMerchantPrivateKeyFile(filePath);
		config.setApiV3Key("74106caf5a5cDeec20fe1b2C3A29823f");
		config.setNotifyUrl("http://lookersci.com/api/pay/wechatcb");

		String timeStamp = "1650639723";
		String nonceStr = "4eylpi356096osno0qtaszkjc3757ak6";
		String prepayId = "wx22230203755713a84b8dab5239f3700000";
		String signStr = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepayId + "\n";
		System.out.println(signStr);
		WxV3PayImpl wxV3Pay = new WxV3PayImpl(config);
		String s = wxV3Pay.getWechatBaseV3Pay().doSign(signStr);
		System.out.println("------------------");
		System.out.println(s);
		System.out.println("------------------");
	}

}