if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function WithdrawPanel(){
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-withdraw-box').appendTo(panel);

	$('<div>').addClass('fb-withdraw-head').text("会员账号").append("：").append(GLOBAL.User.name).appendTo(box);

	var body = $('<div>').addClass('fb-withdraw-body').appendTo(box);
	$('<b>').text('亚洲用户请输入以下资料').appendTo(body);

	new TextInput('drawAmount','drawAmount').appendTo(body);
	new TextInput('drawPswd','drawPswd').appendTo(body).children('input').attr("type","password");
	new TextInput('debitCardUser','debitCardUser').appendTo(body);
	var cardType = new TextInput('debitCardType','debitCardType').appendTo(body);
	var sls = $('<select>').addClass('fb-input-text');
	sls.append('<option value="0" selected="selected">中国工商银行</option>\
				<option value="1">中国农业银行</option>\
				<option value="2">中国建设银行</option>\
				<option value="3">中国招商银行</option>\
				<option value="4">中国交通银行</option>\
				<option value="5">中国邮政银行</option>\
				<option value="6">中国民生银行</option>\
				<option value="7">中国银行</option>');
	cardType.children('input').replaceWith(sls);
	new TextInput('debitCardNum','debitCardNum').appendTo(body);
	var zone = new TextInput('debitCardZone','debitCardZone').appendTo(body);
	var sls = $('<select>').addClass('fb-input-text');
	sls.append('<option value="北京" selected="selected">北京</option>\
				<option value="上海">上海</option>\
				<option value="天津">天津</option>\
				<option value="广东">广东</option>\
				<option value="重庆">重庆</option>\
				<option value="河北">河北</option>\
				<option value="河南">河南</option>\
				<option value="江苏">江苏</option>\
				<option value="浙江">浙江</option>\
				<option value="山东">山东</option>\
				<option value="山西">山西</option>\
				<option value="广西">广西</option>\
				<option value="福建">福建</option>\
				<option value="内蒙古">内蒙古</option>\
				<option value="黑龙江">黑龙江</option>\
				<option value="辽宁">辽宁</option>\
				<option value="吉林">吉林</option>\
				<option value="新疆">新疆</option>\
				<option value="甘肃">甘肃</option>\
				<option value="宁夏">宁夏</option>\
				<option value="陕西">陕西</option>\
				<option value="湖北">湖北</option>\
				<option value="湖南">湖南</option>\
				<option value="江西">江西</option>\
				<option value="四川">四川</option>\
				<option value="贵州">贵州</option>\
				<option value="云南">云南</option>\
				<option value="西藏">西藏</option>\
				<option value="青海">青海</option>\
				<option value="海南">海南</option>\
				<option value="安徽">安徽</option>\
				<option value="香港">香港</option>\
				<option value="澳门">澳门</option>\
				<option value="其他">其他</option>');
	zone.children('input').replaceWith(sls);
	new TextInput('debitCardCity','debitCardCity').appendTo(body);
	new TextInput('debitCardLoc','debitCardLoc').appendTo(body);

	var notice = $('fb-withdraw-notice').appendTo(box);

	var valiCodeBox = new TextInput('drawCAPTCHA','CAPTCHA').appendTo(body);
	var valiCodeDiv = $('<span>').addClass('fb-input-text');
	var valiCodeText = $('<input type="text">').addClass('fb-input-text').width(80).appendTo(valiCodeDiv).focus(function(){
		$.ajax({
			type: "get",
			contentType: "application/json",
			url: "/game/captcha",
			success: function(data) {
				 $("#drawCAPTCHA").find('img').attr("src",data);
			}
		});
	});
	var valiCodePic = $('<img>').addClass('fb-valicode-pic').attr("src","./img/CAPTCHA.png").appendTo(valiCodeDiv);
	valiCodeBox.children('input').replaceWith(valiCodeDiv);

	var checkNotice = $('<div>').addClass('fb-input-box').appendTo(body);
	$('<span>').addClass('fb-input-label').appendTo(checkNotice);
	$('<input  id="drawCheckForNotice" type="checkbox" checked="ture">').addClass('fb-input-text').appendTo(checkNotice);
	$('<label>').text("我已查看提款须知，并已清楚了解了").appendTo(checkNotice);

	var checkBindingUser = $('<div>').addClass('fb-input-box').appendTo(body);
	$('<span>').addClass('fb-input-label').appendTo(checkBindingUser);
	$('<input id="drawBindingCard" type="checkbox" checked="ture">').addClass('fb-input-text').appendTo(checkBindingUser);
	$('<label>').text("绑定此取款信息").appendTo(checkBindingUser);

	var submit = $('<div>').addClass('fb-input-box').appendTo(body);
	$('<span>').addClass('fb-input-label').appendTo(submit);
	$('<button>').text('提交').appendTo(submit).click(function(){
		var drawAmount = $('#drawAmount').children('input').val();
		if(drawAmount== ""){
			new Warning("取款额度不能为空","#0F0","normal");
			return;
		}
		var drawPswd = $('#drawPswd').children('input').val();
		if(drawPswd== ""){
			new Warning("取款密码不能为空","#0F0","normal");
			return;
		}
		var debitCardUser = $('#debitCardUser').children('input').val();
		if(debitCardUser== ""){
			new Warning("开户姓名不能为空","#0F0","normal");
			return;
		}
		var debitCardType = $('#debitCardType').children('select').val();
		var debitCardNum = $('#debitCardNum').children('input').val();
		if(debitCardNum== ""){
			new Warning("开户卡号不能为空","#0F0","normal");
			return;
		}
		var debitCardZone = $('#debitCardZone').children('select').val();
		var debitCardCity = $('#debitCardCity').children('input').val();
		if(debitCardCity== ""){
			new Warning("开户市不能为空","#0F0","normal");
			return;
		}
		var debitCardLoc = $('#debitCardLoc').children('input').val();
		if(debitCardLoc== ""){
			new Warning("开户网点不能为空","#0F0","normal");
			return;
		}
		if(!$('#drawCheckForNotice').prop("checked")){
			new Warning("请确认已经阅读过开户协议","#0F0","normal");
			return;
		}
		var drawCAPTCHA = $('#drawCAPTCHA').find("input").val();
		if( drawCAPTCHA == ""){
			new Warning("验证码不能为空","#0F0","normal");
			return;
		}

		var withdrawData={
				userId:GLOBAL.User.uuid,
				point:drawAmount,
				cardType:debitCardType,
				cardNum:debitCardNum,
				cardArea:debitCardZone,
				cardCity:debitCardCity,
				cardSite:debitCardLoc,
				username:GLOBAL.User.username,
				name:debitCardUser,
				captcha:drawCAPTCHA,
				password:drawPswd};
		$.ajax({
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(withdrawData),
			url: "/game/withdrawals",
			success: function(data) {
				$('#drawAmount').children('input').val("");
				$('#drawPswd').children('input').val("");
				$('#debitCardUser').children('input').val("");
				$('#debitCardNum').children('input').val("");
				$('#debitCardCity').children('input').val("");
				$('#debitCardLoc').children('input').val("");
				$('#drawCAPTCHA').find("input").val("");
				alert("取款单已经提交，请等待审核！");
			}
		});
		$('#drawCAPTCHA').find("input").val("");
	});

	var directions = $('<div>').addClass('fb-withdraw-directions').appendTo(box);
	$('<h1>').text("提款须知").append("：").appendTo(directions);
	$('<p>').text('1 、银行账户持有人姓名必须与在菲博国际输入的姓名一致，否则无法申请提款。').appendTo(directions);
	$('<p>').text('2 、大陆各银行帐户均可申请提款。').appendTo(directions);
	$('<p>').text('3 、每个会员账户（北京时间）24小时只提供一次提款。').appendTo(directions);
	$('<p>').text('4 、买彩后未经全额投注提彩申请不予受理。').appendTo(directions);
	$('<p>').text('5 、每位客户只可以使用一张银行卡进行提款,如需要更换银行卡请与客服人员联系.否则提款将被拒绝。').appendTo(directions);
	$('<p>').text('6 、为保障客户资金安全，菲博国际有可能需要用户提供电话单，银行对账单或其它资料验证，以确保客户资金不会被冒领。').appendTo(directions);

	$('<h1>').text("到账时间：").append("：").appendTo(directions);
	$('<p>').text('使用中国工商银行任何时间均可随意申请提款，5分钟至30分钟到账。').appendTo(directions);

	panel.find('.fb-panel-mask').remove();
	return panel;
}

GLOBAL.Panels['WithdrawPanel'] = WithdrawPanel;