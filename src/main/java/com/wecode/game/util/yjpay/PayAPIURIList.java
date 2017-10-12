package com.wecode.game.util.yjpay;

public enum PayAPIURIList {
	

	/**
	 * 手机wap网页收银台支付地址（通用），支持借记卡与信用卡(返回手机页面收银台方式)
	
	   PAYAPI_MOBILE_PAY("/mobile/pay/request"),
	

	 * 手机wap网页收银台支付地址（借记卡）

	   PAYAPI_MOBILE_PAY_DEBIT("/mobile/pay/bankcard/debit/request"),
	

	 * 手机wap网页收银台支付地址（信用卡）

	*PAYAPI_MOBILE_PAY_CREDIT("/mobile/pay/bankcard/credit/request"),
	 */
	
	
	
	
	
	
	
	
	/**
	 * PC 网页收银台
	 */
	PCWEB_PAY("/api/pay/request"),
	/**
	 * 移动终端网页收银台
	 */
	PAYWEB_PAY("/api/pay/request"),
	
	
	/**
	 * 绑卡支付请求
	 */
	PAYAPI_BINDCARDPAY("/api/bankcard/bind/pay/request"),
	
	/**
	 * 发送短信校验码
	 */
	PAYAPI_SENDVALIDATECODE("/api/validatecode/send"),
	
	/**
	 * 校验短信验证码，确认支付
	 */
	PAYAPI_CONFIRMPAY("/api/async/bankcard/pay/confirm/validatecode"),

	/**
	 * 解绑
	 */
	PAYAPI_UNBINDCARD("/api/bankcard/unbind"),
	/**
	 * 查下支付结果
	 */
	PAYAPI_QUERYORDER("/api/query/order"),

	/**
	 * 获取绑卡列表
	 */
	PAYAPI_BINDLIST("/api/bankcard/bind/list"),

	/**
	 * 根据银行卡卡号检查银行卡是否可以使用一键支付
	 */
	PAYAPI_BANKCARDCHECK("/api/bankcard/check");

	private String value;

	private PayAPIURIList(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
