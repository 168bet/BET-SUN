if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}
function openBlank(action,data,n){
    var form = $("<form/>").attr('action',action).attr('method','post');
    form.attr('target','_blank');
    var input = '';
    $.each(data, function(i,n){
        input += '<input type="hidden" name="'+ i +'" value="'+ n +'" />';
    });
    form.append(input).appendTo("body").css('display','none').submit();
    form.remove();
}

function OnlineChargePanel(){
	function TextInfo(label,value){
		var info = $('<div>').addClass('fb-ocharge-text');
		$('<label>').i18ntext(label).append("：").appendTo(info);
		$('<span>').text(value).appendTo(info);
		return info;
	}
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-ocharge-box').appendTo(panel);
	$('<strong>').text("在线充值").append("：").appendTo(box);
	var infoBox = $('<div>').addClass('fb-ocharge-info').appendTo(box);
	var user = GLOBAL.User;
	new TextInfo("accountName",user.username).appendTo(infoBox);
	new TextInfo("telephone",user.phone).appendTo(infoBox);
	new TextInfo("userBalance",user.point).appendTo(infoBox);

	var chargeAmount = $('<div>').addClass('fb-ocharge-text').appendTo(infoBox);
	$('<label>').text("充值金额").append("：").appendTo(chargeAmount);
	$('<input type="text" id="chargeAmount">').width(80).appendTo(chargeAmount);
	$('<strong>').text(" 元").appendTo(chargeAmount);

	var commit = $('<div>').addClass('fb-ocharge-text').appendTo(infoBox);
	$('<label>').text("　").appendTo(commit);
	$('<button>').text("马上充值").appendTo(commit).click(function(){
		var chargeAmount = $('#chargeAmount').val();
		if( Number(chargeAmount).toString() ==="NaN"){
			new Warning("充值金额的必须是个数字!","#0F0","normal");
			return;
		}
		var data = {};
		data.amount = chargeAmount * 100;
		data.uid = GLOBAL.User.uuid;
		data.uname = GLOBAL.User.username;
		$.ajax({
			type: 'post',
			url: "/game/charge",
			contentType: "application/json",
			data: JSON.stringify(data),
			dataType: "json",
			success: function(response){
				openBlank(response.url,response);
			}
		});
	});


	$('<strong>').text("在线充值说明").append("：").appendTo(box);
	$('<p>').text("(1).请按表格填写准确的在线充值信息,确认提交后会进入选择的银行进行在线付款!").appendTo(box);
	$('<p>').text("(2).交易成功后请点击返回支付网站可以查看您的订单信息!").appendTo(box);
	$('<p>').text("(3).如有任何疑问,您可以联系 在线客服,菲博国际为您提供365天×24小时不间断的友善和专业客户咨询服务!").appendTo(box);

	panel.find('.fb-panel-mask').remove();
	return panel;
}

function CompanyChargePanel(){
	function TextInfo(label,value){
		var info = $('<div>').addClass('fb-input-box');
		$('<label>').addClass('fb-input-label').i18ntext(label).append("：").appendTo(info);
		$('<span>').addClass('fb-input-text').text(value).appendTo(info);
		return info;
	}
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-ccharge-box').appendTo(panel);
	$('<strong>').text("尊敬的会员，您好！").appendTo(box);
	$('<p>').text("    会员可以通过网银转账/银行汇款/ATM转帐等方式充值，\
		请在金额转出之后填写详细的汇款信息进行充值，我们财务部将会根据以\
		下的汇款金额时间标准为您确认添加金额到您的会员账户。").appendTo(box);
	$('<p>').text("会员汇款时请在金额后面加个尾数(例如：转账金额 1000.66 \
		或 1000.88)，以减少您的等待时间以免导致过投注时间。对于恶意重复提\
		交虚假汇款信息的会员(包括多次出现未存款先提交等)，我们将会进行冻结\
		账号处理，如造成不便，敬请谅解！！！").appendTo(box);
	$('<p>').text("    会员存款银行账户信息将会实时更新于此页面，请您在每\
		次存款之前先登录会员账户查询该页面是否有新的存款银行账户信息通知，\
		感谢您的支持和配合！！！").appendTo(box);
	$('<p>').text("　").appendTo(box);

	var accounts = [{bank:"中国农业银行",id:"6228************871",name:"中国农业银行",city:"中国农业银行"},
	{bank:"中国农业银行",id:"6228************871",name:"中国农业银行",city:"中国农业银行"},
	{bank:"中国农业银行",id:"6228************871",name:"中国农业银行",city:"中国农业银行"}];

	$('<strong>').text("汇款转账详细账户资料").append("：").appendTo(box);
	var accountTable = $('<table>').addClass('fb-ccharge-table').appendTo(box);
	for(var i = 0;i < accounts.length; ++i){
		var acc = accounts[i];
		var line = $('<tr>').appendTo(accountTable);
		$('<td>').append(acc.bank).append("：").appendTo(line);
		$('<td>').css("color","#00F").append(acc.id).appendTo(line);
		$('<td>').text("开户名").append("：").appendTo(line);
		$('<td>').css("color","#00F").text(acc.name).appendTo(line);
		$('<td>').text("开户行所在城市").append("：").appendTo(line);
		$('<td>').css("color","#00F").text(acc.city).appendTo(line);
	}
	$('<p>').appendTo(box);
	$('<strong>').text("温馨提示").append("：").appendTo(box);
	$('<p>').text("一、请在金额转出之后务必填写网页下方的汇款信息表格，以便\
		我们财务人员能及时为您确认添加金额到您的会员账户。").appendTo(box);
	$('<p>').text("二、本公司最低汇款金额为100元，每次存款赠送2%红利，无上限\
		(红利金额按百赠送)。").appendTo(box);
	$('<p>').text("三、本公司不支持跨行转账方式进行汇款。").appendTo(box);

	$('<hr>').appendTo(box);
	$('<p>').text("此存款信息只是您汇款详情的提交，并非代表存款，您需要自己通\
		过ATM或网银转帐到本公司提供的账户后，填写提交此信息，待工作人员审核冲\
		值！").appendTo(box);

	$('<strong>').text("汇款信息提交").append("：").appendTo(box);

	var infoBox = $('<div>').addClass('fb-ccharge-info').appendTo(box);
	var user = GLOBAL.User;
	new TextInfo("accountName",user.username).appendTo(infoBox);
	new TextInput('depositeAmount','depositeAmount').appendTo(infoBox);
	var cardType = new TextInput('debitCardType','debitCardType').appendTo(infoBox);
	var sls = $('<select>').addClass('fb-input-text');
	sls.append('<option value="0" selected="selected">中国工商银行</option>\
				<option value="1">中国农业银行</option>\
				<option value="2">中国建设银行</option>');
	cardType.children('input').replaceWith(sls);

	new TextInput('depositeDate','depositeDate').appendTo(infoBox);
	var zone = new TextInput('depositeMethod','depositeMethod').appendTo(infoBox);

	var sls = $('<select>').addClass('fb-input-text');
	sls.append('<option value="银行柜台">银行柜台</option>\
                <option value="ATM现金">ATM现金</option>\
                <option value="ATM卡转">ATM卡转</option>\
                <option value="网银转账">网银转账</option>');
	zone.children('input').replaceWith(sls);
	new TextInput('depositeLoc','depositeLoc').appendTo(infoBox);

	var notice = $('fb-withdraw-notice').appendTo(box);

	var valiCodeBox = new TextInput('depositeCAPTCHA','CAPTCHA').appendTo(infoBox);
	var valiCodeDiv = $('<span>').addClass('fb-input-text');
	var valiCodeText = $('<input type="text">').addClass('fb-input-text').width(80).appendTo(valiCodeDiv).focus(function(){
		$.ajax({
			type: "get",
			contentType: "application/json",
			url: "/game/captcha",
			success: function(data) {
				 $("#depositeCAPTCHA").find('img').attr("src",data);
			}
		});
	});
	var valiCodePic = $('<img>').addClass('fb-valicode-pic').attr("src","./img/CAPTCHA.png").appendTo(valiCodeDiv);
	valiCodeBox.children('input').replaceWith(valiCodeDiv);

	//#Debug配置
	$('#depositeDate').children("input").val("2014-08-22 05:44:36");
	var submit = $('<div>').addClass('fb-input-box').appendTo(infoBox);
	$('<span>').addClass('fb-input-label').appendTo(submit);
	$('<button>').text('提交').appendTo(submit).click(function(){
		var chargeAmount = $('#depositeAmount').children("input").val();
		if( Number(chargeAmount).toString() ==="NaN"){
			new Warning("充值金额的必须是个数字!","#0F0","normal");
			return;
		}

		var debitCardType = $('#depositeDate').children("select").val();

		var depositeDate = $('#depositeDate').children("input").val();
		if( depositeDate == ""){
			new Warning("汇款日期不能为空","#0F0","normal");
			return;
		}
		depositeDate = new Date(depositeDate);
		if( depositeDate.toString() == "Invalid Date"){
			new Warning("请输入正确的汇款日期","#0F0","normal");
			return;
		}

		var depositeLoc = $('#depositeLoc').children("input").val();
		if( depositeLoc == ""){
			new Warning("汇款地点不能为空","#0F0","normal");
			return;
		}

		var depositeCAPTCHA = $('#depositeCAPTCHA').find("input").val();
		if(depositeCAPTCHA==""){
			new Warning("验证码不能为空","#0F0","normal");
			return;
		}
		window.console.log('debug');
		var data = {};
		data.point = chargeAmount ;
		data.userId = GLOBAL.User.uuid;
		data.username = GLOBAL.User.username;
		data.cardType = debitCardType;
		data.cardSite = depositeLoc;
		data.createdTime = Date.parse(depositeDate);
		data.info = "汇款方式:" + depositeMethod;
		data.captcha = depositeCAPTCHA;
		$.ajax({
			type: 'post',
			url: "/game/deposite/manual",
			contentType: "application/json",
			data: JSON.stringify(data),
			dataType: "json",
			success: function(response){
				alert("提交成功，请等待管理员审核！");
				$('#depositeAmount').children("input").val("");
				$('#depositeDate').children("input").val("");
				$('#depositeLoc').children("input").val("");
				$('#depositeCAPTCHA').find("input").val("");
			}
		});
	});


	$('<strong>').text("汇款信息提交说明").append("：").appendTo(box);
	$('<p>').text("(1).请按表格填写准确的汇款转账信息,确认提交后相关财务人\
		员会即时为您查询入款情况!").appendTo(box);
	$('<p>').text("(2).请您在转账金额后面加个尾数,例如:转账金额 1000.66 \
		或 1000.88 等,以便相关财务人员更快确认您的转账金额并充值!").appendTo(box);
	$('<p>').text("(3).如有任何疑问,您可以联系 在线客服,菲博国际为您提供365\
		天×24小时不间断的友善和专业客户咨询服务!").appendTo(box);

	panel.find('.fb-panel-mask').remove();
	return panel;
}

function ChargePanel(){
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-charge-box').appendTo(panel);
	$('<h1>').text("在线支付").append("：").appendTo(box);
	var pChargeBtn = $('<button type="button">').text("在线支付").addClass('clickable').click(function(){
		new OnlineChargePanel().data('fb').exchange();
	});
	$('<div>').append($('<span>').text("使用网银在线支付;免手续费;即时到帐.")).append($('<label>').append(pChargeBtn)).appendTo(box);
	$('<span>').text("超过10000RMB建议请使用公司入款!").appendTo(box);

	box.append('<p>&nbsp;</p><p>&nbsp;</p>');

	$('<h1>').text("公司入款：通过银行卡汇款").append("：").appendTo(box);
	var pChargeBtn = $('<button type="button">').text("公司入款").addClass('clickable').click(function(){
		new CompanyChargePanel().data('fb').exchange();
	});
	$('<div>').append($('<span>').text("3~5分钟到账，赠送2%红利")).append($('<label>').append(pChargeBtn)).appendTo(box);
	$('<span>').text("请每次入款前登录会员核对银行账号是否变更！").appendTo(box);
	
	box.append('<p>&nbsp;</p><p>&nbsp;');
	$('<b>').text('特别提示').append('：').appendTo(box);
	$('<p>').text('请客户在进行存款操作前首先确认持有的银行卡是否具有网上支付功能。若未开通或有其他疑问，请按银行 "帮助中心"详细了解及处理。').appendTo(box);
	$('<p>').appendTo(box);
	$('<b>').text('注意事项').append('：').appendTo(box);
	$('<p>').text('1、你所使用的存款银行需要开通网上银行业务。').appendTo(box);
	$('<p>').text('2、当你在存款过程中遇到任何问题，请随联系我们在线客服咨询或邮箱。').appendTo(box);

	return panel;
}

GLOBAL.Panels['ChargePanel'] = ChargePanel;