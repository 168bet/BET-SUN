if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function ApplyForAgencyPanel(){
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-applyagency-box').appendTo(panel);

	new TextInput('cusName', 'memberAccount', true).appendTo(box);
	new TextInput('cusTel', 'realName', true).appendTo(box);
	new TextInput('cusEmail', 'telephone', true).appendTo(box);
	new TextInput('cusMsgSub', 'email', true).appendTo(box);
	
	var textarea = $('<div id="cusMsgTtl">').addClass('fb-input-box').css({"margin":"0 auto","display":"inline-block"}).appendTo(box);
	var label = $('<span>').i18ntext('applyReason').append("：").addClass('fb-input-label').css({"float":"left"}).appendTo(textarea);
	var tdiv =  $('<div>').css({"width":"200px","float":"left"}).appendTo(textarea);
	var text = $('<textarea >').addClass('fb-input-text required').width(300).height(100).appendTo(tdiv);


	var valiCodeBox = new TextInput('drawCAPTCHA','CAPTCHA').appendTo(box);
	var valiCodeDiv = $('<span>').addClass('fb-input-text');
	var valiCodeText = $('<input type="text">').addClass('fb-input-text').width(80).appendTo(valiCodeDiv);
	var valiCodePic = $('<img>').addClass('fb-valicode-pic').attr("src","./img/CAPTCHA.png").appendTo(valiCodeDiv);
	valiCodeBox.children('input').replaceWith(valiCodeDiv);

	$('<p>').text("已经年满18岁，在此网站所有活动并没有抵触本人所在国家所管辖的法律，且同意").appendTo(box);

	$('<hr>').appendTo(box);

	$('<div>').css({"clear":"both"}).appendTo(box);
	$('<button>').addClass('fb-applyagency-commit').i18ntext('commit').appendTo(box);
	
	panel.find('.fb-panel-mask').remove();
	return panel;
}

GLOBAL.Panels['ApplyForAgencyPanel'] = ApplyForAgencyPanel;