package com.wecode.game.util.yjpay;

import java.util.ResourceBundle;

public class YJPayConfig {

	private static ResourceBundle resb1 = ResourceBundle.getBundle("payapi");

	// 从配置文件读取易宝分配的公钥
	public static String YibaoPublicKey = resb1.getString("payapi.paytest_yibao_publickey");

	// 从配置文件读取商户自己的私钥
	public static String MerchantPrivateKey = resb1.getString("payapi.paytest_merchant_privatekey");

	// 商户自己随机生成的AESkey
	public static String MerchantAesKey = RandomUtil.getRandom(16);

	// 商户账户编号
	public static String MerchantAccount = resb1.getString("payapi.paytest_merchantaccount");

	// 从配置文件读取支付API接口URL前缀
	public static String UrlPrefix = resb1.getString("payapi.payweb_urlprefix");

}
