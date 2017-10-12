if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function CustomerServicePanel(){
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-customer-box').appendTo(panel);
	$('<h1>').i18ntext('customerService').appendTo(box);
	$('<p>').i18ntext('reposeASAP').appendTo(box);
	$('<hr>').appendTo(box);

	new TextInput('cusName', 'name', true).appendTo(box);
	new TextInput('cusTel', 'telephone').appendTo(box);
	new TextInput('cusEmail', 'email', true).appendTo(box);
	new TextInput('cusMsgSub', 'massageSubject').appendTo(box);
	
	var textarea = $('<div id="cusMsgTtl">').addClass('fb-input-box').width(360).css({"margin":"0 auto","display":"inline-block"}).appendTo(box);
	var label = $('<span>').i18ntext('massageContent').addClass('fb-input-label').addClass('required').css({"float":"left"}).appendTo(textarea);
	var tdiv =  $('<div>').css({"width":"200px","float":"left"}).appendTo(textarea);
	var text = $('<textarea >').addClass('fb-input-text required').width(300).height(150).appendTo(tdiv);

	$('<div>').css({"clear":"both"}).appendTo(box);
	$('<div>').addClass('fb-customer-login btn').i18ntext('commit').appendTo(box);
	
	panel.find('.fb-panel-mask').remove();
	return panel;
}

GLOBAL.Panels['CustomerServicePanel'] = CustomerServicePanel;