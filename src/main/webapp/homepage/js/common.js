function PageTurning( cb ){
	var pageTurning = $('<div>').addClass();
}

function FbButton(value){
	return $('<div>').addClass('fb-button btn').i18ntext(value);
}

function TextInput(id, name, required){
	var input = $('<div id=' + id + '>').addClass('fb-input-box');
	var label = $('<span>').i18ntext(name).append("：").addClass('fb-input-label').appendTo(input);
	var text = $('<input type="text" >').addClass('fb-input-text').appendTo(input);
	if(typeof(required) != "undefined" && required){
		label.addClass('required');
		text.addClass('required');
	}
	return input;
}
function PasswordStrength(id){
	var box = $('<div>').attr('id',id).addClass('fb-pswdstnth-box');
	$('<span>').addClass('fb-pswdstnth-label').i18ntext('passwordStrength').appendTo(box);
	$('<span>').addClass('fb-pswdstnth-strenth').i18ntext('passwordStrengthWeak').appendTo(box);
	$('<span>').addClass('fb-pswdstnth-strenth').i18ntext('passwordStrengthNormal').appendTo(box);
	$('<span>').addClass('fb-pswdstnth-strenth').i18ntext('passwordStrengthStrong').appendTo(box);
	return box;
}

function Warning(msg, color, msgType){
	var warning = $('<div>').addClass('fb-float-warning');
	if (msgType == 'normal'){
		warning.text(msg);
	}else if(msgType == "i18n"){
		warning.i18ntext(msg);
	}else{
		warning.i18ntext(msg);
	}

	if(typeof(color) != "undefined"){
		warning.css('color',color);
	}
	$('body').prepend(warning);

	warning.animate({opacity:"0.8",marginTop:140},2000,function(){
		warning.remove();
	});
	return warning;
}

$.ajaxSetup({
	error: function(XMLHttpRequest, textStatus, errorThrow) {
		if(window) window.console.log(XMLHttpRequest.responseJSON);
		var info = GLOBAL.Lang.errorInfo[XMLHttpRequest.status];
		if(info == null || info == undefined){
			if(window) window.console.log("该错误信息未注册，错误编码" + XMLHttpRequest.status);
			info = XMLHttpRequest.responseText;
		}
		new Warning(info,'#0F0','normal');
	}
});

function Breaker(name){
	var breaker = $('<div>').addClass('fb-breaker');
	var title = $('<div>').addClass('fb-breaker-titile').i18ntext(name).appendTo(breaker);
	var line = $('<div>').addClass('fb-breaker-line').append('<hr>').appendTo(breaker);
	return breaker;
}