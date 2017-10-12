package com.wecode.game.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wecode.game.bean.Charge;
import com.wecode.game.dao.ChargeDao;
import com.wecode.game.exception.ResultToBeanException;
import com.wecode.game.identified.ChargeState;
import com.wecode.game.util.yjpay.AES;
import com.wecode.game.util.yjpay.EncryUtil;
import com.wecode.game.util.yjpay.RSA;
import com.wecode.game.util.yjpay.YJPayConfig;

@Service("chargeService")
public class ChargeService {

	private static Logger log = Logger.getLogger(ChargeService.class);
	private static SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");

	@Resource(name = "chargeDao")
	private ChargeDao chargeDao;

	public boolean addCharge(Charge charge) {
		return chargeDao.addCharge(charge);
	}

	// 创建唯一订单
	public String createOrderNum(String uid) {
		return "FB" + sm.format(new Date()) + uid.replaceAll("-", "");
	}

	public String getYJPayEncryptKey() {
		try {
			return RSA.encrypt(YJPayConfig.MerchantAesKey, YJPayConfig.YibaoPublicKey);
		} catch (Exception e) {
			log.error("RSA加密失败，加密内容：" + YJPayConfig.MerchantAesKey + ","
					+ YJPayConfig.YibaoPublicKey, e);
			return null;
		}
	}

	public String getYJPayEncryptData(Charge charge, String callback, String fcallback) {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("merchantaccount", YJPayConfig.MerchantAccount);// 商户账号，在property里配置
		map.put("amount", charge.amount);// 充值总额，单位：分
		map.put("currency", 156);// 币种类型，目前仅支持RMB(156)
		map.put("identityid", charge.uid);// 用户id
		map.put("identitytype", 2); // http://mobiletest.yeepay.com/file/doc/pubshow?doc_id=14#hm_26
		map.put("orderid", charge.orderNum); // 本地订单号
		// map.put("terminalid", "05-16-DC-59-C2-34");// 非必填项
		// map.put("terminaltype", "1");// 非必填项
		map.put("productcatalog", "1");// 商品类别码:虚拟产品（1）
		map.put("productdesc", "菲博内部代币");
		map.put("productname", "菲博-内部代币");
		map.put("transtime", charge.createTime);
		map.put("userip", charge.ip);
		map.put("callbackurl", callback);
		map.put("fcallbackurl", fcallback);
		map.put("userua", charge.userAgent);
		map.put("paytypes", "1|2");// 1为借记卡2为贷记卡，1|2表示支持借记卡和贷记卡
		map.put("orderexpdate", 60);// 订单过期时间，如60分钟
		// 生成RSA签名
		String sign = EncryUtil.handleRSA(map, YJPayConfig.MerchantPrivateKey);

		map.put("sign", sign);
		// 生成data
		String info = JSON.toJSONString(map);
		return AES.encryptToBase64(info, YJPayConfig.MerchantAesKey);
	}

	public boolean checkDecryptAndSign(String data, String encryptkey) {
		try {
			return EncryUtil.checkDecryptAndSign(data, encryptkey, YJPayConfig.YibaoPublicKey,
					YJPayConfig.MerchantPrivateKey);
		} catch (Exception e) {
			log.error("前面验证失败，签名内容：" + data + "," + encryptkey, e);
			return false;
		}
	}

	public String decryptData(String data, String encryptkey) {
		try {
			String yb_aeskey = RSA.decrypt(encryptkey, YJPayConfig.MerchantPrivateKey);
			return AES.decryptFromBase64(data, yb_aeskey);
		} catch (Exception e) {
			log.error("解密失败，解密内容：" + data + "," + encryptkey, e);
			return null;
		}
	}

	public Charge updateCharge(Map<String, String> map) throws ResultToBeanException {
		Charge charge = chargeDao.getChargeByOrderNum(map.get("orderid"));
		if (charge.state == ChargeState.PAYED) {
			return null;
		}
		try {
			charge.amount = Long.parseLong(map.get("amount"));
			charge.ybOrderNum = map.get("yborderid");
			charge.state = ChargeState.PAYED;
			charge.payTimes++;
			charge.extendInfo = "bank:" + map.get("bank") + ";lastno:" + map.get("lastno")
					+ ";identityid:" + map.get("identityid") + ";identitytype:"
					+ map.get("identitytype");
			charge.payTime = System.currentTimeMillis() / 1000;
			if (!chargeDao.updateCharge(charge)) {
				return null;
			}
			return charge;
		} catch (Exception e) {
			return null;
		}
	}
}
