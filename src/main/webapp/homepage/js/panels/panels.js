if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function Panel(){
	var panel = $('<div>');
	panel.data('fb',{
		exchange:function(params){
			if($('.fb-panel-loc').children().size() != 0){
				if(!GLOBAL.HistoryPanel){
					GLOBAL.HistoryPanel = [];
				}
				var prePanel = $('.fb-panel-loc').children().detach();
				var hstr = GLOBAL.HistoryPanel;
				hstr.push(prePanel);
				if(hstr.length > 5){
					hstr.shift();
				}
			}
			panel.appendTo($('.fb-panel-loc'));
			this.load(params);
		},
		load:function(params){
			panel.find('.fb-panel-mask').remove();
		}
	});
	return panel;
}

function BasePanel(){
	var panel = new Panel().addClass('fb-panel-base');
	var loadMask = $('<div>').addClass('fb-panel-mask').appendTo(panel);
	var loading = $('<p>').i18ntext("loading...");
	function movingleft(){
		loading.animate({'margin-left':-20},1000,movingright);
	}
	function movingright(){
		loading.animate({'margin-left': 20},1000,movingleft);
	}
	movingleft();

	$('<div class="fb-panel-loader">').append(loading).append("<span/><span/><span/><span/><span/>").appendTo(loadMask);

	return panel;
}

function ErrorPanel(){
	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-error-box').appendTo(panel);
	$('<h1>').i18ntext('panelError').appendTo(box);
	return panel;
}
GLOBAL.Panels['ErrorPanel'] = ErrorPanel;

GLOBAL.Panels.getPanel = function(panelName){
	if(!this[panelName] && !this.loadPanelFile(panelName)){
		return new ErrorPanel();
	}else{
		return new this[panelName]();
	}
}

GLOBAL.Panels.loadPanelFile = function(panelName){
	if(!panelName){
		return;
	}
	var loadsuccee = false;
	$.ajax({
	  url: "./js/panels/" + panelName + ".js",
	  dataType: "script",
	  async: false,
	  success: function(){
	  	loadsuccee = true;
	  },
	  error:function(XMLHttpRequest, textStatus, errorThrown){
	  	if(window.console) window.console.log("加载" + this.url+ " 文件错误：" + errorThrown.stack);
		loadsuccee = false;
	  }
	});
	$.include("./css/panels/" + panelName + ".css");
	return loadsuccee;
}

GLOBAL.Panels.showPanel = function(panelName,params){
	if(this.Current == panelName){
		return;
	}
	this.getPanel(panelName).data('fb').exchange(params);
	this.Current = panelName;
}
GLOBAL.Panels.Current = null;