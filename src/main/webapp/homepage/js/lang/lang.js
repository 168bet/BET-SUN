if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Lang){
	GLOBAL.Lang = {};
}

GLOBAL.Lang.init = function(lang){
	if(!lang){
		lang = 'zh_cn';
	}
	$.ajax({
	  url: "./js/lang/" + lang + "_error.js",
	  dataType: "script",
	  async: false,
	  cache: false,
	  success: function(){
	  	// do nothing.	
	  	GLOBAL.Lang.lang = lang;
	  },
	  error:function(XMLHttpRequest, textStatus, errorThrown){
		$.ajax({
		  url: "./js/lang/zh_cn_error.js",
		  dataType: "script",
		  async: false,
		  cache: false,
		  success: function(){
		  	// do nothing.	
	  		GLOBAL.Lang.lang = lang;
		  }
		});
	  }
	});
	$.ajax({
	  url: "./js/lang/" + lang + ".js",
	  dataType: "script",
	  async: false,
	  cache: false,
	  success: function(){
	  	// do nothing.	
	  	GLOBAL.Lang.lang = lang;
	  },
	  error:function(XMLHttpRequest, textStatus, errorThrown){
		$.ajax({
		  url: "./js/lang/zh_cn.js",
		  dataType: "script",
		  async: false,
		  cache: false,
		  success: function(){
		  	// do nothing.	
	  		GLOBAL.Lang.lang = lang;
		  }
		});
	  }
	});
}

GLOBAL.Lang.get = function(txt){
	if(!this.words){
		if(window.console) window.console.log("#Error# 国际化文件初始失败。");
	}
	if(typeof(txt) == "undefined"){
		return "";
	}

	if(!this.words[txt]){
		return "Error:\"" + txt + "\" didn't regist in \"" + this.lang + "\"";
	}else{
		return this.words[txt];
	}
}

$.prototype.i18ntext = function(txt){
	return this.text(GLOBAL.Lang.get(txt));
}