if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function RegisterPanel(){
	function TextInput(id, name, pdesc, tag, alt){
		var input = $('<div id=' + id + '>').addClass('fb-register-input-box');
		var label = $('<span>').i18ntext(name).addClass('fb-register-input-label').appendTo(input);
		var text = $('<input type="text" >').addClass('fb-register-input-text').appendTo(input);
		var desc = $('<span>').addClass('fb-register-input-desc').appendTo(input);
		if(typeof(desc) != "undefined"){
			desc.i18ntext(pdesc);
		}
		if(tag == "required"){
			text.addClass('required');
			desc.addClass('required');
		}else if(tag == "attention"){
			text.addClass('attention');
			desc.addClass('attention');
		}
		if(typeof(alt) != "undefined"){
			text.alt(alt);
		}
		return input;
	}

	function PasswordInput(id, name, desc, tag, strenthLabel){
		var input = new TextInput(id, name, desc, tag);
		var changeEvent = function(event){
			var pswd = $(this).val();
			if(pswd.length > 16){
				pswd = pswd.substring(0,16);
				$(this).val(pswd)
			}
			var strenth = 0;
			window.console.log(pswd.length);
			var labels = $('#' + strenthLabel).children('.fb-pswdstnth-strenth');
			labels.removeClass('weak').removeClass('normal').removeClass('strong');
			if(pswd.length == 0 ){
				return;
			}
			if (pswd.length < 8 && pswd.length > 0) {
				labels.eq(0).addClass('weak');
				return;
			}
			if(pswd.match('^.*[0-9]+.*$') != null){
				strenth ++;
			}
			if(pswd.match('^.*[a-z]+.*$') != null){
				strenth ++;
			}
			if(pswd.match('^.*[A-Z]+.*$') != null){
				strenth ++;
			}
			if(pswd.match('^.*[^0-9a-zA-Z]+.*$') != null){
				strenth ++;
			}
			if (strenth == 1) {
				labels.eq(0).addClass('weak');
			}else if(strenth == 2){
				labels.eq(1).addClass('normal');
			}else if(strenth > 2){
				labels.eq(2).addClass('strong');
			}
		};
		input.children('input').attr("type","password").keyup(changeEvent).keydown(changeEvent);
		return input;
	}

	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-register-box').appendTo(panel);

	new Breaker('loginData').appendTo(box);
	new TextInput('memAcc','memberAccount',undefined,'required').appendTo(box);
	new PasswordInput('memLoginPswd','loginPswd','loginPswdDesc','attention','memLoginPswdStrnth').appendTo(box);
	new PasswordStrength('memLoginPswdStrnth').appendTo(box);
	new PasswordInput('memLoginPswdCfm','loinPswdCfm','loginPswdDesc','attention').appendTo(box);

	new Breaker('personalData').appendTo(box);
	new TextInput('memCdName','cardholderName','cardholderNameDesc','attention').appendTo(box);
	new TextInput('memTel','telephone','telephoneInputDesc','required').appendTo(box);
	new TextInput('memIntroducer','introducer','introducerInputDesc').appendTo(box);

	// new Breaker('passwordProtectionData').appendTo(box);
	// var proBox = new TextInput('memPswdProQustn','passwordProtectionQuestion','plsSelectProQuestion','required').appendTo(box);
	// var sls = $('<select>').addClass('fb-register-input-text');
	// var g_preQuestion = GLOBAL.ProQuestion;
	// for(var i = 0; i < g_preQuestion; ++i){
	// 	$('<option value=' + g_preQuestion[i] + ' selected>' + i + '</option>').appendTo(sls);
	// }
	// proBox.children('input').replaceWith(sls);
	// new TextInput('memPswdProAnsr','passwordProtectionAnswer','plsInputProAnswer','required').appendTo(box);

	new Breaker('withdrawData').appendTo(box);
	new PasswordInput('memDrawPswd','drawPswd','drawPswdDesc','required','memDrawPswdStrnth').appendTo(box);
	new PasswordStrength('memDrawPswdStrnth').appendTo(box);
	new PasswordInput('memDrawPswdCfm','drawPswdCfm','drawPswdDesc','required').appendTo(box);

	var valiCodeBox = new TextInput('memCAPTCHA','CAPTCHA','plsInputCAPTCHA','required').appendTo(box);
	var valiCodeDiv = $('<span>').addClass('fb-register-input-text');
	var valiCodeText = $('<input type="text">').addClass('fb-register-input-text').width(80).appendTo(valiCodeDiv).focus(function(){
		$.ajax({
			type: "get",
			contentType: "application/json",
			url: "/game/captcha",
			success: function(data) {
				 $('.fb-register-valicode-pic').attr("src",data);
			}
		});
	});
	var valiCodePic = $('<img>').addClass('fb-register-valicode-pic').attr("src","./img/CAPTCHA.png").appendTo(valiCodeDiv);
	valiCodeBox.children('input').replaceWith(valiCodeDiv);

	var agreement = $('<div>').addClass('fb-register-display-box').appendTo(box);
	$('<input type="checkbox" id="memAgree">').appendTo(agreement);
	$('<span>').i18ntext('agreement').css('padding','0 15px 0 5px').appendTo(agreement);
	var protocol = $('<a>').i18ntext('accountAgreement').appendTo(agreement);
	protocol.before('" ');
	protocol.after(' "');

	var commit = new FbButton('commit');
	commit.click(function (e) {
		var username = $('#memAcc').children('input').val();
		if(username== ""){
			new Warning("账号名不能为空","#0F0","normal");
			return;
		}
		if(username.match('^[0-9a-zA-Z]{5,12}$') == null){
			new Warning("账号名只能由5~12位数字或密码组成!","#0F0","normal");
			return;
		}
		var memLoginPswd = $('#memLoginPswd').children('input').val();
		if(memLoginPswd == ""){
			new Warning("登陆密码不能为空","#0F0","normal");
			return;
		}
		if(memLoginPswd.match('^.{8~16}$') == null){
			new Warning("登陆密码由8~16位字符组成!","#0F0","normal");
			return;
		}

		if($('#memLoginPswdCfm').children('input').val() == ""){
			new Warning("登陆确认密码不能为空","#0F0","normal");
			return;
		}
		if($('#memLoginPswdCfm').children('input').val() != memLoginPswd){
			new Warning("两次的登陆密码不一致！","#0F0","normal");
			return;
		}
		var memCdName = $('#memCdName').children('input').val();
		if(memCdName == ""){
			new Warning("持卡人姓名不能为空","#0F0","normal");
			return;
		}
		var memTel = $('#memTel').children('input').val();
		if(memTel == ""){
			new Warning("联系人电话不能为空","#0F0","normal");
			return;
		}
		if(memTel.match('^1\\d{10}$') == null){
			new Warning("请输入正确的联系电话","#0F0","normal");
			return;
		}
		var memDrawPswd = $('#memDrawPswd').children('input').val();
		if(memDrawPswd == ""){
			new Warning("取款密码不能为空","#0F0","normal");
			return;
		}
		if(memDrawPswd.match('^[0-9a-zA-Z]{8~16}$') == null){
			new Warning("取款密码由8~16位字符组成!","#0F0","normal");
			return;
		}

		if($('#memDrawPswdCfm').children('input').val() == ""){
			new Warning("登陆确认密码不能为空","#0F0","normal");
			return;
		}
		if($('#memDrawPswdCfm').children('input').val() != memDrawPswd){
			new Warning("两次的取款密码不一致！","#0F0","normal");
			return;
		}
		if(!$('#memAgree').prop("checked")){
			new Warning("请确认已经阅读过开户协议","#0F0","normal");
			return;
		}
		var memCAPTCHA = $('#memCAPTCHA').find('input').val();
		if(memCAPTCHA==""){
			new Warning("验证码不能为空","#0F0","normal");
			return;
		}
		var memIntroducer = $('#memIntroducer').children('input').val();
		memIntroducer = memIntroducer == ""?null:memIntroducer;
		
		var postData={username: username,
			password:memLoginPswd,
			name:memCdName,
			phone:memTel,
			agent:memIntroducer,
			moneyPwd:memDrawPswd,
			captcha:memCAPTCHA};
		$.ajax({
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(postData),
			url: "/game/register",
			success: function(data) {
				$('.fb-login-box').data('fb').reload(data);
				GLOBAL.Panels.showPanel("IndexPanel");
			}
		});
	});

	box.append($('<div>').addClass('fb-register-display-box').append(commit));

	return panel;
}


GLOBAL.Panels['RegisterPanel'] = RegisterPanel;