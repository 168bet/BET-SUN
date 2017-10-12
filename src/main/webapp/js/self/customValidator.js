//账号验证
$.validator.addMethod("isAccount", function(value, element) {
	var regex = /^[a-zA-Z0-9_]+$/;
	return this.optional(element) || (regex.test(value));
});

//充值金额验证
$.validator.addMethod("isChargeNum", function(value, element) {
	var regex = /^[0-9]*[1-9][0-9]*$/;
	return this.optional(element) || (regex.test(value));
});

//充值金额重复验证
$.validator.addMethod("isRepChargeNum", function(value, element, param) {
	var chargeNum = $(param).val();
	return value == chargeNum;
});

//密码验证
$.validator.addMethod("isPassword", function(value, element) {
	var regex = /^[^_][A-Za-z]*[a-z0-9A-Z_]*$/;
	return this.optional(element) || ((regex.test(value)) && value.length >= 8 && value.length <= 16);
});

//密码重复验证
$.validator.addMethod("isRepPassword", function(value, element, param) {
	var password = $(param).val();
	return value == password;
});

//洗码佣金比例验证
$.validator.addMethod("isWashCode", function(value, element) {
	var regex = /^(\d{1,2}(\.\d{1,2})?|100|100\.0|100\.00)$/;
	return this.optional(element) || regex.test(value);
});

//返点验证
$.validator.addMethod("isRebate", function(value, element) {
	var regex = /^(\d{1,2}(\.\d{1,2})?|100|100\.0|100\.00)$/;
	return this.optional(element) || regex.test(value);
});

//真人点数验证
$.validator.addMethod("isRealCount", function(value, element) {
	var regex = /^[0-9]*[1-9][0-9]*$/;
	return this.optional(element) || (regex.test(value));
});

//可赢上限验证
$.validator.addMethod("isWinLimit", function(value, element) {
	var regex = /^\d+$/;
	return this.optional(element) || (regex.test(value));
});

//姓名验证
$.validator.addMethod("isRealName", function(value, element) {
	var regex = /^([\u4e00-\u9fa5]+|([a-z]+\s?)+)$/;
	return this.optional(element) || (regex.test(value));
});

//取款密码验证
$.validator.addMethod("isMoneyPwd", function(value, element) {
	var regex = /^[^_][A-Za-z]*[a-z0-9A-Z_]*$/;
	return this.optional(element) || ((regex.test(value)) && value.length >= 8 && value.length <= 16);
});

//借记卡号验证
$.validator.addMethod("isCardNum", function(value, element) {
	var regex = /^[0-9]*[1-9][0-9]*$/;
	return this.optional(element) || (regex.test(value));
});

//开户市验证
$.validator.addMethod("isCardCity", function(value, element) {
	var regex = /^([\u4e00-\u9fa5]+|([a-z]+\s?)+)$/;
	return this.optional(element) || (regex.test(value));
});

//开户网点验证
$.validator.addMethod("isCardSite", function(value, element) {
	var regex = /^([\u4e00-\u9fa5]+|([a-z]+\s?)+)$/;
	return this.optional(element) || (regex.test(value));
});

//取款金额验证
$.validator.addMethod("isMoney", function(value, element) {
	var regex = /^[0-9]*[1-9][0-9]*$/;
	return this.optional(element) || (regex.test(value));
});

//结束日期验证
$.validator.addMethod("isEndDate", function(value, element, param) {
	var data = $(param).val();
	return comparetime(data, value);
});

//验证码验证
$.validator.addMethod("isValidateCode", function(value, element) {
	var regex = /^[a-zA-Z0-9]{4}$/;
	return this.optional(element) || (regex.test(value));
});

//复选框勾选验证
$.validator.addMethod("isChecked", function(value, element) {
	return element.checked;
});

//比较时间 格式 yyyy-mm-dd hh:mi:ss
function comparetime(beginTime, endTime) {
	var beginTimes = beginTime.substring(0, 10).split('-');
	var endTimes = endTime.substring(0, 10).split('-');
	beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
	endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);
	var a = (Date.parse(endTime) - Date.parse(beginTime)) / 3600 / 1000;
	if (a < 0) {
		return false;
	} else if (a > 0) {
		return true;
	} else if (a == 0) {
		return true;
	} else {
		return 'exception'
	}
}