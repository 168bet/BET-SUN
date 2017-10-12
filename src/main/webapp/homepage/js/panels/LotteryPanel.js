if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function LotteryPanel(){
	var resultBox = function(){
		var box = $('<div>').addClass('fb-lottery-box').appendTo(panel);
		
		return box;
	}

	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-lottery-box').appendTo(panel);
	panel.data('fb').load = function(){
		var tbox = $('<div>').addClass('fb-lottery-box');
		var rest = $('<div>').addClass('fb-lottery-rest').appendTo(tbox);
		$('<h1>').text("00:00").appendTo(rest);
		var btns = $('<div>').addClass('fb-lottery-buttons').appendTo(tbox);
		$('<button>').addClass('fb-lottery-result').appendTo(btns);
		$('<button>').addClass('fb-lottery-rules').appendTo(btns);
		panel.find('.fb-lottery-box').replaceWith(tbox);
		panel.find('.fb-panel-mask').remove();
	}
	return panel;
}
GLOBAL.Panels['LotteryPanel'] = LotteryPanel;
