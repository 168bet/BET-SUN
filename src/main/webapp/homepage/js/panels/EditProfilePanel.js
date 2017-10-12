if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function EditProfilePanel(){
	function TextInput(id, name){
		var input = $('<div id=' + id + '>').addClass('fb-edit-input-box');
		var label = $('<span>').i18ntext(name).append("：").addClass('fb-edit-input-label').appendTo(input);
		var text = $('<input type="text" >').addClass('fb-edit-input-text').appendTo(input);
		return input;
	}

	function PasswordInput(id, name){
		var input = new TextInput(id, name);
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
			if (pswd.length < 8) {
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
			if (strenth <= 1) {
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
	var box = $('<div>').addClass('fb-edit-box').appendTo(panel);

	$('<h1>').i18ntext('loginData').appendTo(box);
	new PasswordInput('memLoginPswdOri','loginPswdOri').appendTo(box);
	new PasswordInput('memLoginPswdNew','loginPswdNew').appendTo(box);
	new PasswordStrength('memLoginPswdStrnth', 'memLoginPswd').appendTo(box);
	new PasswordInput('memLoginPswdCfm','loinPswdCfm').appendTo(box);

	$('<h1>').i18ntext('withdrawData').appendTo(box);
	new PasswordInput('memDrawPswdOri','drawPswdOri').appendTo(box);
	new PasswordInput('memDrawPswdNew','drawPswdNew').appendTo(box);
	new PasswordStrength('memDrawPswdStrnth', 'memLoginPswd').appendTo(box);
	new PasswordInput('memDrawPswdCfm','drawPswdCfm').appendTo(box);

	// $('<h1>').i18ntext('passwordProtectionData').appendTo(box);
	// var proBox = new TextInput('memPswdProQustn','passwordProtectionQuestion').appendTo(box);
	// proBox.children('input').replaceWith($('<lable>').text(GLOBAL.User.protectionQuestion));
	// new TextInput('memPswdProAnsr','passwordProtectionAnswer').appendTo(box);

	$('<h1>').i18ntext('CAPTCHA').appendTo(box);
	var valiCodeBox = new TextInput('memCAPTCHA','CAPTCHA').appendTo(box);
	var valiCodeDiv = $('<span>').addClass('fb-edit-input-text');
	var valiCodeText = $('<input type="text">').addClass('fb-edit-input-text').width(80).appendTo(valiCodeDiv).focus(function(){
		$.ajax({
			type: "get",
			contentType: "application/json",
			url: "/game/captcha",
			success: function(data) {
				 $('.fb-edit-valicode-pic').attr("src",data);
			}
		});
	});
	var valiCodePic = $('<img>').addClass('fb-edit-valicode-pic').attr("src","./img/CAPTCHA.png").appendTo(valiCodeDiv);
	valiCodeBox.children('input').replaceWith(valiCodeDiv);

	var commit = $('<button>').i18ntext('commit');
	commit.click(function (e) {
		var memLoginPswdOri = $('#memLoginPswdOri').children('input').val();
		if(memLoginPswdOri== ""){
			new Warning("原始登陆密码不能为空","#0F0","normal");
			return;
		}
		var memLoginPswdNew = $('#memLoginPswdNew').children('input').val();
		if(memLoginPswdNew== ""){
			new Warning("新登陆密码不能为空","#0F0","normal");
			return;
		}
		if(memLoginPswdNew.match('^.{8~16}$') == null){
			new Warning("登陆密码由8~16位字符组成!!","#0F0","normal");
			return;
		}
		if($('#memLoginPswdCfm').children('input').val()== ""){
			new Warning("登陆确认密码不能为空","#0F0","normal");
			return;
		}
		if($('#memLoginPswdCfm').children('input').val() != memLoginPswdNew){
			new Warning("两次的登陆密码不一致!","#0F0","normal");
			return;
		}

		var memDrawPswdOri = $('#memDrawPswdOri').children('input').val();
		if(memDrawPswdOri== ""){
			new Warning("原始登陆密码不能为空","#0F0","normal");
			return;
		}
		var memDrawPswdNew = $('#memDrawPswdNew').children('input').val();
		if(memDrawPswdNew== ""){
			new Warning("新登陆密码不能为空","#0F0","normal");
			return;
		}
		if(memDrawPswdNew.match('^.{8~16}$') == null){
			new Warning("取款密码由8~16位字符组成!!","#0F0","normal");
			return;
		}
		if($('#memDrawPswdCfm').children('input').val()== ""){
			new Warning("取款确认密码不能为空","#0F0","normal");
			return;
		}
		if($('#memDrawPswdCfm').children('input').val() != memDrawPswdNew){
			new Warning("两次的取款密码不一致!","#0F0","normal");
			return;
		}
		var memCAPTCHA = $('#memCAPTCHA').find('input').val();
		if(memCAPTCHA == ""){
			new Warning("验证码不能为空","#0F0","normal");
			return;
		}

		var pswddata={prePassword:memLoginPswdOri,
				newPassword:memLoginPswdNew,
				preMoneyPswd:memDrawPswdOri,
				newMoneyPswd:memDrawPswdNew,
				captcha:memCAPTCHA};
		window.console.log(pswddata);
		$.ajax({
			type: "put",
			contentType: "application/json",
			data: JSON.stringify(pswddata),
			url: "/game/member/password",
			success: function(data) {
				$('#memLoginPswdOri').children('input').val("");
				$('#memLoginPswdNew').children('input').val("");
				$('#memLoginPswdCfm').children('input').val("");
				$('#memDrawPswdOri').children('input').val("");
				$('#memDrawPswdNew').children('input').val("");
				$('#memDrawPswdCfm').children('input').val("");
				$('#memCAPTCHA').find('input').val("");
				alert("密码修改成功");
			}
		});
	});
	box.append($('<div>').addClass('fb-edit-display-box').append(commit));

	var notice = $('<div>').addClass('fb-edit-notice').appendTo(box);
	$('<b>').text("忘记密码？").appendTo(notice);
	$('<p>').text("如果您忘记了密码，请您扫描身份证件发送到菲博国际邮箱，并与客服联系。").appendTo(notice);
	$('<p>').text("为了保证会员的资金安全，请您谅解要扫描身份证件验证您的身份。").appendTo(notice);
	$('<p>').text("也请您放心，您的资料绝对保密，谢谢您对菲博国际的支持！").appendTo(notice);
	$('<p>').text("菲博国际永久邮箱：fb0088@hotmail.com").appendTo(notice);

	return panel;
}


GLOBAL.Panels['EditProfilePanel'] = EditProfilePanel;