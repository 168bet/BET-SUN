//Date 函数
Date.prototype.toChineseDateString = function() {
	return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日";
}

Date.prototype.toChineseTimeString = function() {
	var hour = this.getHours() < 10? "0" + this.getHours() : this.getHours();
	var min = this.getMinutes() < 10? "0" + this.getMinutes() : this.getMinutes();
	var sec = this.getSeconds() < 10? "0" + this.getSeconds() : this.getSeconds();
	return this.toChineseDateString() + " " + hour + ":" + min + ":" + sec;
}

Date.prototype.toSimpleDateString = function() {
	return this.getFullYear() + '-' + (this.getMonth() + 1) + '-' + this.getDate();
}

Date.prototype.toSimpleTimeString = function() {
	var hour = this.getHours() < 10? "0" + this.getHours() : this.getHours();
	var min = this.getMinutes() < 10? "0" + this.getMinutes() : this.getMinutes();
	var sec = this.getSeconds() < 10? "0" + this.getSeconds() : this.getSeconds();
	return this.toSimpleDateString() + " " + hour + ":" + min + ":" + sec;
}
Date.prototype.getWeekday = function()  
{   
	var Week = ['日','一','二','三','四','五','六'];
    return '星期' + Week[this.getDay()];  
}  

//String 函数
String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.ltrim=function(){
	return this.replace(/(^\s*)/g,"");
}

String.prototype.rtrim=function(){
	return this.replace(/(\s*$)/g,"");
}

Tools = {};
Tools.Css = {};
Tools.Css.Transform = function(obj, deg){
	obj.css({
		"transform": "rotate(" + deg + "deg)",
		"-o-transform": "rotate(" + deg + "deg)",
		"-webkit-transform": "rotate(" + deg + "deg)",
		"-moz-transform": "rotate(" + deg + "deg)"
	});
}

$.extend({
	includePath: '',
	include: function(file) {
		var files = typeof file == "string" ? [file]:file;
		for (var i = 0; i < files.length; i++) {
			var name = files[i].replace(/^\s|\s$/g, "");
			var att = name.split('.');
			var ext = att[att.length - 1].toLowerCase();
			var isCSS = ext == "css";
			var tag = isCSS ? "link" : "script";
			var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' ";
			var link = (isCSS ? "href" : "src") + "='" + $.includePath + name + "'";
			if ($(tag + "[" + link + "]").length == 0) $("head").append($("<" + tag + attr + link + "></" + tag + ">"));
        }
   }
});
